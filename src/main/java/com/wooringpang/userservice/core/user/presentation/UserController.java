package com.wooringpang.userservice.core.user.presentation;

import com.wooringpang.userservice.core.user.presentation.request.*;
import com.wooringpang.userservice.core.user.presentation.response.FindUserResponse;
import com.wooringpang.userservice.core.user.presentation.response.JoinUserResponse;
import com.wooringpang.userservice.core.user.presentation.response.SaveUserResponse;
import com.wooringpang.userservice.core.user.presentation.response.UpdateUserResponse;
import com.wooringpang.userservice.core.user.dto.UserListDto;
import com.wooringpang.userservice.core.user.dto.UserSearchCondition;
import com.wooringpang.userservice.core.user.service.UserService;
import com.wooringpang.userservice.global.config.TokenProvider;
import com.wooringpang.userservice.global.exception.BusinessMessageException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    private final BCryptPasswordEncoder passwordEncoder;

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
    public ResponseEntity<SaveUserResponse> saveUser(@RequestBody @Validated SaveUserRequest request) {
        //validate

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SaveUserResponse(userService.save(request.toParam())));
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
     * refresh token 과 일치하는 사용자가 잇으면 access toekn 을 새로 발급하여 리턴한다.
     */
    @PutMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        tokenProvider.refreshToken(refreshToken, response);
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
        return userService.existsEmail(request.getEmail(), request.getSignId());
    }

    /**
     * 사용자 회원 가입
     */
    @PostMapping("/join")
    public ResponseEntity<JoinUserResponse> join(@RequestBody @Validated JoinUserRequest request) {
        Long joinId = userService.join(request.toParam(passwordEncoder));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new JoinUserResponse(joinId));
    }

    /**
     * 사용자 비밀번호 찾기
     */
    @PostMapping("/password/find")
    public Boolean findPassword(@RequestBody @Validated UserFindPasswordSaveRequest request) {
        return userService.findPassword(request);
    }

    /**
     * 사용자 회원정보 변경
     */
    @PutMapping("/info/{signId}")
    public String updateInfo(@PathVariable String signId, @RequestBody @Validated UserUpdateInfoRequest request) {
        final String authUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authUserId.equals(signId)) {
            throw new BusinessMessageException("에러");
        }
        return userService.updateInfo(signId, request);
    }

    /**
     * 사용자 회원탈퇴
     */
    @PostMapping("/leave")
    public Boolean leave(@RequestBody @Validated UserVerifyRequest request) {
        final String signId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.leave(signId, request);
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
