package com.woorinpang.userservice.domain.auth.presentation;

import com.woorinpang.userservice.domain.auth.application.AuthService;
import com.woorinpang.userservice.domain.user.application.UserService;
import com.woorinpang.userservice.domain.user.presentation.user.request.UserJoinRequest;
import com.woorinpang.userservice.domain.user.presentation.user.response.UserJoinResponse;
import com.woorinpang.userservice.global.common.cotroller.CommonController;
import com.woorinpang.userservice.global.common.json.JsonResponse;
import com.woorinpang.userservice.global.config.TokenProvider;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController extends CommonController {

    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final UserService userService;

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
//        Boolean isAuth = authService.isAuthorization(roles, httpMethod, requestPath);

        log.info("[isAuthorization={}] authentication.isAuthenticated()={}, userId={}, httpMethod={}, requestPath={}, roleList={}", isAuth, authentication.isAuthenticated(), username, httpMethod, requestPath, roles);

        return isAuth;
    }


    //TODO 로그아웃


}
