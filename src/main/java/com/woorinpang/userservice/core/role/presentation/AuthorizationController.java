package com.woorinpang.userservice.core.role.presentation;

import com.woorinpang.userservice.core.role.dto.AuthorizationListDto;
import com.woorinpang.userservice.core.role.dto.AuthorizationSearchCondition;
import com.woorinpang.userservice.core.role.presentation.request.SaveAuthorizationRequest;
import com.woorinpang.userservice.core.role.presentation.request.UpdateAuthorizationRequest;
import com.woorinpang.userservice.core.role.presentation.response.FindAuthorizationResponse;
import com.woorinpang.userservice.core.role.presentation.response.SaveAuthorizationResponse;
import com.woorinpang.userservice.core.role.presentation.response.UpdateAuthorizationResponse;
import com.woorinpang.userservice.core.role.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authorizations")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    /**
     * 인가 여부 확인
     */
    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public Boolean isAuthorization(@RequestParam("httpMethod") String httpMethod, @RequestParam("requestPath") String requestPath) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .collect(Collectors.toList());

        //사용자 아이디로 조회
        //return authorizationService.isAuthorization(username, httpMethod, requestPath);

        //권한으로 조회
        Boolean isAuthorization = authorizationService.isAuthorization(roles, httpMethod, requestPath);

        log.info("[isAuthorization={}] authentication.isAuthenticated()={}, username={}, httpMethod={}, requestPath={}, roles={}",
                isAuthorization, authentication.isAuthenticated(), username, httpMethod, requestPath, roles);

        return isAuthorization;
    }

    /**
     * 인가 페이지 목록 조회
     */
    @GetMapping
    public ResponseEntity<Page<AuthorizationListDto>> findPageAuthorizations(
            AuthorizationSearchCondition condition,
            @PageableDefault(sort = "sort", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorizationService.findPageAuthorizations(condition, pageable));
    }

    /**
     * 인가 단건 조회
     */
    @GetMapping("/{authorizationId}")
    public ResponseEntity<FindAuthorizationResponse> findAuthorization(@PathVariable("authorizationId") Long authorizationId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new FindAuthorizationResponse(authorizationService.findAuthorization(authorizationId)));
    }

    /**
     * 인가 다음 정렬 순서 조회
     */
    @GetMapping("/sort/next")
    @ResponseStatus(HttpStatus.OK)
    public Integer findNextSort() {
        return authorizationService.findNextSort();
    }

    /**
     * 인가 등록
     */
    @PostMapping
    public ResponseEntity<SaveAuthorizationResponse> save(@RequestBody @Validated SaveAuthorizationRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SaveAuthorizationResponse(authorizationService.save(request.toEntity()).getId()));
    }

    /**
     * 인가 수정
     */
    @PutMapping("/{authorizationId}")
    public ResponseEntity<UpdateAuthorizationResponse> update(@PathVariable("authorizationId") Long authorizationId,
                                                              @RequestBody @Validated UpdateAuthorizationRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UpdateAuthorizationResponse(authorizationService.update(authorizationId, request).getId()));
    }

    /**
     * 인가 삭제
     */
    @DeleteMapping("/{authorizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("authorizationId") Long authorizationId) {
        authorizationService.delete(authorizationId);
    }
}