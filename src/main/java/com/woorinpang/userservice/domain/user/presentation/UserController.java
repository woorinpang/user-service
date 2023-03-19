package com.woorinpang.userservice.domain.user.presentation;

import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.dto.UserListDto;
import com.woorinpang.userservice.domain.user.dto.UserSearchCondition;
import com.woorinpang.userservice.domain.user.presentation.dto.UserDtoMapper;
import com.woorinpang.userservice.domain.user.presentation.dto.request.SaveUserRequest;
import com.woorinpang.userservice.domain.user.presentation.response.FindUserResponse;
import com.woorinpang.userservice.domain.user.presentation.response.JoinUserResponse;
import com.woorinpang.userservice.domain.user.presentation.response.SaveUserResponse;
import com.woorinpang.userservice.domain.user.presentation.response.UpdateUserResponse;
import com.woorinpang.userservice.domain.user.application.UserService;
import com.woorinpang.userservice.domain.user.presentation.request.*;
import com.woorinpang.userservice.global.exception.BusinessMessageException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * 유저 목록 조회
     */
    @GetMapping
    public ResponseEntity<Page<UserListDto>> findUsers(UserSearchCondition condition, Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findUsers(condition, pageable));
    }

    /**
     * 유저 단건 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<FindUserResponse> findUser(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new FindUserResponse(userService.findUser(userId)));
    }

    /**
     * 유저 정보 입력
     */
    @PostMapping
    public ResponseEntity<SaveUserResponse> saveUser(@RequestBody @Valid SaveUserRequest request) {
        //validate
        request.validate();

        ResponseEntity<SaveUserResponse> body = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SaveUserResponse(userService.save(request.toCommand())));

        return body;
    }

    /**
     * 유저 정보 수정
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable("userId") Long userId, @RequestBody @Validated UpdateUserRequest request) {
        //validate
        userService.update(userId, request.toParam());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UpdateUserResponse(userService.findUser(userId).getId()));
    }

    /**
     * 사용자 소셜 정보 조회
     */
    public ResponseEntity<SocialUserResponse> social(@RequestBody @Validated SocialUserRequest request) {
        SocialUserResponse response = userService.getSocialUserInfo(request.getProvider(), request.getToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * 이메일 중복 확인
     */
    @PostMapping("/exists")
    public Boolean existsEmail(@RequestBody @Validated UserEmailRequest request) {
        return userService.existsEmail(request.getEmail(), request.getUsername());
    }

    /**
     * 사용자 회원 가입
     */
    @PostMapping("/join")
    public ResponseEntity<JoinUserResponse> join(@RequestBody @Validated JoinUserRequest request) {
        Long joinId = userService.join(request.toParam());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new JoinUserResponse(joinId));
    }

    /**
     * 사용자 회원정보 변경
     */
    @PutMapping("/info/{userId}")
    public String updateInfo(@PathVariable Long userId, @RequestBody @Validated UserUpdateInfoRequest request) {
        final String authUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authUserId.equals(userId)) {
            throw new BusinessMessageException("에러");
        }
        User findUser = this.userService.findUser(userId);
        return userService.updateInfo(findUser.getUsername(), request);
    }

    /**
     * 사용자 회원탈퇴
     */
    @PostMapping("/leave")
    public Boolean leave(@RequestBody @Validated UserVerifyRequest request) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.leave(username, request);
    }

    /**
     * 사용자 삭제
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean delete(@PathVariable("userId") Long userId) {
        return userService.deleteUser(userId);
    }
}
