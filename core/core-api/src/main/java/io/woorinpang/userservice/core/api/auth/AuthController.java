package io.woorinpang.userservice.core.api.auth;

import io.woorinpang.userservice.config.TokenProvider;
import io.woorinpang.userservice.core.api.user.request.ExistsUsernameRequest;
import io.woorinpang.userservice.core.api.user.request.JoinUserRequest;
import io.woorinpang.userservice.core.domain.user.service.AuthService;
import io.woorinpang.userservice.support.common.json.DefaultSuccessIdResponse;
import io.woorinpang.userservice.support.common.json.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
     * 아이디 중복확인(username) - 로그인 없이
     */
    @PostMapping("/username/exists")
    public ResponseEntity<JsonResponse> existsUsername(
            @RequestBody @Valid ExistsUsernameRequest request
    ) {
        return ResponseEntity.ok(JsonResponse.OK(authService.existsUsername(request.getUsername())));
    }

    /**
     * 회원가입 -> 로그인 필요없음
     */
    @PostMapping("/join")
    public ResponseEntity<JsonResponse> joinUser(
            @RequestBody @Valid JoinUserRequest request
    ) {
        long successId = authService.userJoin(request.toUserLogin(passwordEncoder), request.toUserInfo());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(JsonResponse.CREATED(new DefaultSuccessIdResponse(successId)));
    }

    /**
     * refresh token 과 일치하는 사용자가 있으면 access token 을 새로 발급하여 리턴한다.
     */
    @PutMapping("/token/refresh")
    public ResponseEntity<JsonResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        tokenProvider.refreshToken(refreshToken, response);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK());
    }

    @GetMapping("/check")
    public Boolean isAuthorization(@RequestParam("httpMethod") String httpMethod, @RequestParam("requestPath") String requestPath) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::toString).collect(Collectors.toList());

        // 사용자 아이디로 조회
         Boolean isAuth =  authService.isAuthorization(username, httpMethod, requestPath);

        // 권한으로 조회
        //Boolean isAuth = authService.isAuthorization(roles, httpMethod, requestPath);

        log.info("[isAuthorization={}] authentication.isAuthenticated()={}, id={}, httpMethod={}, requestPath={}, roleList={}", isAuth, authentication.isAuthenticated(), username, httpMethod, requestPath, roles);

        return isAuth;
    }

    //TODO 로그아웃
}
