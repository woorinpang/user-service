package com.woorinpang.userservice.core.auth.presentation;

import com.woorinpang.userservice.core.user.application.UserService;
import com.woorinpang.userservice.global.config.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    //private final TokenProvider tokenProvider;

    /**
     * refresh token 과 일치하는 사용자가 잇으면 access toekn 을 새로 발급하여 리턴한다.
     */
    /*@PutMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        tokenProvider.refreshToken(refreshToken, response);
    }*/

    /**
     * 토큰 조회
     */

    /**
     * 로그아웃
     */

}
