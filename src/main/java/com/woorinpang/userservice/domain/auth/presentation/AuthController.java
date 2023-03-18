package com.woorinpang.userservice.domain.auth.presentation;

import com.woorinpang.userservice.domain.auth.presentation.dto.request.SignupUserRequest;
import com.woorinpang.userservice.global.common.json.JsonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/signup")
    public ResponseEntity<JsonResponse> signup(@RequestBody @Valid SignupUserRequest request) {
        //TODO validate

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(JsonResponse.OK());
    }
}
