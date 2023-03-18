package com.woorinpang.userservice.domain.auth.presentation;

import com.woorinpang.userservice.domain.auth.presentation.dto.request.SignupUserRequest;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<JsonResponse> signup(@RequestBody @Valid SignupUserRequest request) {
        //TODO validate

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(JsonResponse.OK());
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
}
