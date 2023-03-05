package com.woorinpang.userservice.core.user.presentation;

import com.woorinpang.common.json.JsonResponse;
import com.woorinpang.userservice.core.user.presentation.request.UserFindPasswordSaveRequest;
import com.woorinpang.userservice.core.user.presentation.request.UserFindPasswordUpdateRequest;
import com.woorinpang.userservice.core.user.presentation.request.UserPasswordMatchRequest;
import com.woorinpang.userservice.core.user.presentation.request.UserPasswordUpdateRequest;
import com.woorinpang.userservice.core.user.service.UserFindPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/password")
public class UserFindPasswordController {

    private final UserFindPasswordService userFindPasswordService;

    /**
     * 사용자 비밀번호 찾기
     */
    @PostMapping("/find")
    public ResponseEntity<JsonResponse> findPassword(@RequestBody @Validated UserFindPasswordSaveRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(userFindPasswordService.findPassword(request)));
    }

    /**
     * 사용자 비밀번호 찾기 유효성 확인
     */
    @GetMapping("/valid/{token}")
    public ResponseEntity<JsonResponse> validPassword(@PathVariable("token") String token) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(userFindPasswordService.validPassword(token)));
    }

    /**
     * 사용자 비밀번호 찾기 변경
     */
    @PutMapping("/change")
    public ResponseEntity<JsonResponse> changePassword(@RequestBody @Validated UserFindPasswordUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(userFindPasswordService.changePassword(request)));
    }

    /**
     * 사용자 비밀번호 변경
     */
    @PutMapping("/update")
    public ResponseEntity<JsonResponse> updatePassword(@RequestBody @Validated UserPasswordUpdateRequest request) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(userFindPasswordService.updatePassword(username, request)));
    }

    /**
     * 사용자 비밀번호 확인
     */
    @PostMapping("/match")
    public ResponseEntity<JsonResponse> matchPassword(@RequestBody @Validated UserPasswordMatchRequest request) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(userFindPasswordService.matchPassword(username, request.getPassword())));
    }
}
