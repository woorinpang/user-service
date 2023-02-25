package com.wooringpang.userservice.core.role.service;

import com.wooringpang.userservice.core.role.domain.Authorization;
import com.wooringpang.userservice.core.role.dto.AuthorizationListDto;
import com.wooringpang.userservice.core.role.dto.AuthorizationSearchCondition;
import com.wooringpang.userservice.core.role.exception.AuthorizationNotFoundException;
import com.wooringpang.userservice.core.role.presentation.request.UpdateAuthorizationRequest;
import com.wooringpang.userservice.core.role.repository.AuthorizationQueryRepository;
import com.wooringpang.userservice.core.role.repository.AuthorizationRepository;
import com.wooringpang.userservice.global.config.GlobalConstant;
import com.wooringpang.userservice.global.service.AbstractService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorizationService extends AbstractService {

    private final AuthorizationQueryRepository authorizationQueryRepository;
    private final AuthorizationRepository authorizationRepository;
    //캐시 관리자
    private final CacheManager cacheManager;

    /**
     * 인가 페이지 목록 조회
     */
    public Page<AuthorizationListDto> findPageAuthorizations(AuthorizationSearchCondition condition, Pageable pageable) {
        return authorizationQueryRepository.findPageAuthorizations(condition, pageable);
    }

    /**
     * 권한의 인가 여부 확인
     * 사용자 서비스 시큐리티 필터에서 호출
     */
    public Boolean isAuthorization(HttpServletRequest request, Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .collect(Collectors.toList());
        List<AuthorizationListDto> authorizations = ((AuthorizationService) AopContext.currentProxy()).findByRoles(roles);

        return isContainMatch(authorizations, request.getMethod(), GlobalConstant.USER_SERVICE_URI + request.getRequestURI());
    }

    /**
     * 권한의 인가 전체 목록 조회
     */
    @Cacheable(value = "cache-user-authorization-by-roles", key = "#roles")
    public List<AuthorizationListDto> findByRoles(List<String> roles) {
        return authorizationQueryRepository.findByRoles(roles);
    }

    /**
     * 권한의 인가 여부 확인
     * gateway 에서 호출
     * <p>
     * Spring Cache 는 Spring AOP 를 이용해서 proxy 로 동작하기 때문에 외부 method 호출만 인터센트해서 작동하고 self-invocation 의 경우 동작하지 않음
     * 스프링에서는 AspectJ 를 권장하지만 Load-Time Weaving 방싱은 퍼포먼스 문제가 있고
     * Compile-Time Weaving 방식은 컴파일 시 수행되는 라이브러리(lombok)와 충돌 문제가 있음
     * AopContext.currentProxy() 를 이용해서 proxy 로 호출하도록 함 - CacheConfig @EnableAspectJAutoProxy(exposeProxy=true)
     */
    public Boolean isAuthorization(List<String> roles, String httpMethod, String requestPath) {
        List<AuthorizationListDto> authorizations = ((AuthorizationService) AopContext.currentProxy()).findByRoles(roles);

        return isContainMatch(authorizations, httpMethod, requestPath);
    }

    /**
     * 사용자의 인가 여부 확인
     */
    public Boolean isAuthorization(String signId, String httpMethod, String requestPath) {
        List<AuthorizationListDto> authorizations = ((AuthorizationService) AopContext.currentProxy()).findBySignId(signId);

        return isContainMatch(authorizations, httpMethod, requestPath);
    }

    /**
     * 사용자의 인가 전체 목록 조회
     */
    @Cacheable(value = "cache-user-authorization-by-signid", key = "#roles")
    public List<AuthorizationListDto> findBySignId(String signId) {
        return authorizationQueryRepository.findBySignId(signId);
    }

    /**
     * 인가 여부 체크
     */
    private Boolean isContainMatch(List<AuthorizationListDto> authorizations, String httpMethod, String requestPath) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        for (AuthorizationListDto authorization : authorizations) {
            if (antPathMatcher.match(authorization.getUrlPatternValue(), requestPath) && authorization.getHttpMethodCode().equals(httpMethod)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 인가 단건 조회
     */
    public Authorization findById(Long authorizationId) {
        return findAuthorization(authorizationId);
    }

    /**
     * 인가 다음 정렬 순서 조회
     */
    public Integer findNextSort() {
        return authorizationQueryRepository.findNextSort();
    }

    /**
     * 인가 등록
     */
    @Transactional
    public Authorization save(Authorization authorization) {
        //동일한 정렬 순서가 존재할 경우 +1
        Optional<Authorization> findAuthorization = authorizationRepository.findBySort(authorization.getSort());
        if (findAuthorization.isPresent()) {
            authorizationQueryRepository.updateSort(authorization.getSort(), null, 1);
        }

        //등록
        Authorization saveAuthorization = authorizationRepository.save(authorization);
        clearAuthorizationCache();
        return saveAuthorization;
    }

    /**
     * 인가 수정
     */
    @Transactional
    public Authorization update(Long authorizationId, UpdateAuthorizationRequest request) {
        Authorization findAuthorization = findAuthorization(authorizationId);

        //정렬 순서가 변경된 경우 사이 구간 정렬 순서 조정
        updateSort(findAuthorization, request);

        //수정
        findAuthorization.update(request.getAuthorizationName(), request.getUrlPatternValue(), request.getHttpMethodCode(), request.getSort());

        return findAuthorization;
    }

    /**
     * 인가 정렬순서 update
     */
    private void updateSort(Authorization authorization, UpdateAuthorizationRequest request) {
        //정렬 순서가 변경괸 경우 사이 구간 정렬 순서 조정
        Integer beforeSeq = authorization.getSort();
        Integer afterSort = request.getSort();

        if (beforeSeq == null) {
            authorizationQueryRepository.updateSort(afterSort, null, 1);
            return;
        }

        if (afterSort == null) {
            authorizationQueryRepository.updateSort(beforeSeq + 1, null, -1);
            return;
        }

        int compareTo = beforeSeq.compareTo(afterSort);

        if (compareTo > 0) {
            authorizationQueryRepository.updateSort(afterSort, beforeSeq - 1, 1);
            return;
        }

        if (compareTo < 0) {
            authorizationQueryRepository.updateSort(beforeSeq + 1, afterSort, -1);
            return;
        }
    }

    /**
     * 인가 삭제
     * 권한 인가도 같이 삭제
     */
    @Transactional
    public void delete(Long authorizationId) {
        Authorization findAuthorization = findAuthorization(authorizationId);

        //삭제
        authorizationRepository.delete(findAuthorization);

        //삭제한 데이터보다 정렬 순서가 더 큰 데이터 -1
        authorizationQueryRepository.updateSort(findAuthorization.getSort() + 1, null, -1);

        clearAuthorizationCache();
    }

    /**
     * 인가 단건 조회 by id
     */
    public Authorization findAuthorization(Long authorizationId) {
        return authorizationRepository.findById(authorizationId)
                .orElseThrow(() -> new AuthorizationNotFoundException(getMessage("valid.notexists.format",
                        new Object[]{getMessage("authorization")})));
    }

    /**
     * 인가 조회 캐시 클리어
     */
    private void clearAuthorizationCache() {
        Cache signidCache = cacheManager.getCache("cache-user-authorization-by-signid");
        if (signidCache != null) signidCache.clear();
        Cache rolesCache = cacheManager.getCache("cache-user-authorization-by-roles");
        if (rolesCache != null) rolesCache.clear();
    }
}
