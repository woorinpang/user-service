package com.woorinpang.userservice.domain.user.presentation;

import com.woorinpang.userservice.domain.user.application.UserService;
import com.woorinpang.userservice.domain.user.application.dto.command.UserLeaveCommand;
import com.woorinpang.userservice.domain.user.presentation.user.request.*;
import com.woorinpang.userservice.domain.user.presentation.user.response.UserInfoResponse;
import com.woorinpang.userservice.domain.user.presentation.user.response.UserJoinResponse;
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

    /**
     * 회원가입 -> 로그인 필요없음
     */
    @PostMapping("/join")
    public ResponseEntity<JsonResponse> userJoin(@RequestBody @Valid UserJoinRequest request) {
        Long userId = userService.join(request.toCommand());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(JsonResponse.CREATED(new UserJoinResponse(userId)));
    }

    /**
     * 내 정보 조회 : 내 정보를 클릭하여 조회한다. -> 로그인 필요함
     */
    @GetMapping("/{userId}")
    public ResponseEntity<JsonResponse> userInfo(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(new UserInfoResponse(userService.findInfo(userId))));
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/{userId}")
    public ResponseEntity<JsonResponse> userUpdateInfo(@PathVariable("userId") Long userId,
                                                       @RequestBody @Valid UserUpdateInfoRequest request) {
        //validate
        request.validate();
        userService.updateInfo(userId, request.toCommand());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK());
    }

    /**
     * 내 비밀번호 확인
     */
    @PostMapping("/password/match")
    public ResponseEntity<JsonResponse> matchPassword(@RequestBody @Valid UserMatchPasswordRequest request) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(userService.matchPassword(username, request.getPassword())));
    }

    /**
     * 아이디 중복확인(username) - 로그인 없이
     */
    @PostMapping("/username/exists")
    public ResponseEntity<JsonResponse> existsUsername(@RequestBody @Valid UserExistsUsernameRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(userService.existsUsername(request.getUsername())));
    }

    /**
     * 사용자 회원탈퇴
    */
    @PostMapping("/leave")
    public ResponseEntity<JsonResponse> leave(@RequestBody @Valid UserLeaveRequest request) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(userService.leave(username, request.toCommand())));
    }
}
