package io.woorinpang.userservice.core.api.controller.auth;

import io.woorinpang.userservice.core.api.config.TokenProvider;
import io.woorinpang.userservice.core.api.controller.auth.request.ExistsUsernameRequest;
import io.woorinpang.userservice.core.api.controller.user.request.JoinUserRequest;
import io.woorinpang.userservice.core.api.support.response.ApiResponse;
import io.woorinpang.userservice.core.api.support.response.DefaultIdResponse;
import io.woorinpang.userservice.core.domain.user.application.AuthService;
import io.woorinpang.userservice.storage.db.core.user.Provider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthService authService;

    /**
     * 이메일 중복확인
     */
    @PostMapping("/username/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsUsername(
            @RequestBody @Valid ExistsUsernameRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(authService.existsEmail(request.getUsername())));
    }

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<DefaultIdResponse>> joinUser(
            @RequestBody @Valid JoinUserRequest request
    ) {
        long successId = authService.joinUser(request.toUserLogin(passwordEncoder), request.getName(), Provider.WOORINPANG);
        return ResponseEntity.ok(ApiResponse.success(new DefaultIdResponse(successId)));
    }

    /**
     * refresh token 과 일치하는 사용자가 있으면 access token 을 새로 발급하여 리턴한다.
     */
    @PutMapping("/token/refresh")
    public ResponseEntity<ApiResponse<Void>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        tokenProvider.createRefreshToken();
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/check")
    public Boolean isAuthorization(@RequestParam("httpMethod") String httpMethod, @RequestParam("requestPath") String requestPath) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::toString).collect(Collectors.toList());

        // 권한으로 조회
        boolean authorization = authService.isAuthorization(email, roles);

        log.info("[isAuthorization={}] authentication.isAuthenticated()={}, email={}, httpMethod={}, requestPath={}, roleList={}", authorization, authentication.isAuthenticated(), email, httpMethod, requestPath, roles);

        return true;
    }
}
