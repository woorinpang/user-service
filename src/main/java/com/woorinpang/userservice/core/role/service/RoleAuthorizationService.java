package com.woorinpang.userservice.core.role.service;

import com.woorinpang.userservice.core.role.dto.RoleAuthorizationListDto;
import com.woorinpang.userservice.core.role.dto.RoleAuthorizationSearchCondition;
import com.woorinpang.userservice.core.role.repository.RoleAuthorizationQueryRepository;
import com.woorinpang.userservice.core.role.repository.RoleAuthorizationRepository;
import com.woorinpang.userservice.core.role.domain.RoleAuthorization;
import com.woorinpang.userservice.global.service.AbstractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleAuthorizationService extends AbstractService {

    private final RoleAuthorizationQueryRepository roleAuthorizationQueryRepository;
    private final RoleAuthorizationRepository roleAuthorizationRepository;
    private final CacheManager cacheManager;

    /**
     * 권한 인가 페이지 목록 조회
     */
    public Page<RoleAuthorizationListDto> findPageRoleAuthorizations(RoleAuthorizationSearchCondition condition, Pageable pageable) {
        if (!hasText(condition.getRoleCode())) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        return roleAuthorizationQueryRepository.findPageRoleAuthorizations(condition, pageable);
    }

    /**
     * 권한 인가 다건 등록
     */
    @Transactional
    public List<RoleAuthorizationListDto> save(List<RoleAuthorization> roleAuthorizations) {
        List<RoleAuthorization> savedRoleAuthorizations = roleAuthorizationRepository.saveAll(roleAuthorizations);

        clearAuthorizationCache();

        return savedRoleAuthorizations.stream()
                .map(roleAuthorization -> RoleAuthorizationListDto.builder()
                        .roleCode(roleAuthorization.getRoleAuthorizationId().getRoleCode())
                        .authorizationId(roleAuthorization.getRoleAuthorizationId().getAuthorizationId())
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 권한 인가 다건 삭제
     */
    @Transactional
    public void delete(List<RoleAuthorization> roleAuthorizations) {
        roleAuthorizationRepository.deleteAll(roleAuthorizations);

        clearAuthorizationCache();
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
