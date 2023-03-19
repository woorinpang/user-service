package com.woorinpang.userservice.domain.user.presentation;

import com.woorinpang.userservice.domain.user.application.UserService;
import com.woorinpang.userservice.domain.user.presentation.user.request.*;
import com.woorinpang.userservice.global.common.json.JsonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    //TODO 회원가입 -> 로그인 필요없음
    @PostMapping("/join")
    public ResponseEntity<JsonResponse> join(@RequestBody @Valid UserJoinRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(JsonResponse.OK());
    }

    //TODO 내 정보 조회 : 내 정보를 클릭하여 조회한다. -> 로그인 필요함
    @GetMapping("/{userId}")
    public ResponseEntity<JsonResponse> Info(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK());
    }

    //TODO 내 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<JsonResponse> updateInfo(@RequestBody @Valid UserUpdateInfoRequest request) {
        //validate

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK());
    }

    //TODO 비밀번호 확인(password) - 로그인 상태
    @PostMapping("/password/match")
    public Boolean matchPassword(@RequestBody @Valid UserMatchPasswordRequest request) {
        final Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        return null;
    }

    //TODO 아이디 중복확인(username) - 로그인 없이
    @PostMapping("/username/exists")
    public Boolean existsUsername(@RequestBody @Valid UserExistsUsernameRequest request) {

        return null;
    }

    //TODO 이메일 중복확인(email) - 로그인 없이
    @PostMapping("/email/exists")
    public Boolean existsEmail(@RequestBody @Valid UserExistsEmailRequest request) {

        return null;
    }

    //TODO 회원탈퇴
    @PostMapping("/leave")
    public Boolean leave(@RequestBody @Valid UserLeaveRequest request) {

        return null;
    }
}
