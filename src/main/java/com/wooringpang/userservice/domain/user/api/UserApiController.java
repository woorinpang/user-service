package com.wooringpang.userservice.domain.user.api;

import com.wooringpang.userservice.domain.user.api.request.SaveUserRequest;
import com.wooringpang.userservice.domain.user.api.request.UpdateUserRequest;
import com.wooringpang.userservice.domain.user.api.response.FindUserResponse;
import com.wooringpang.userservice.domain.user.api.response.SaveUserResponse;
import com.wooringpang.userservice.domain.user.api.response.UpdateUserResponse;
import com.wooringpang.userservice.domain.user.dto.UserListDto;
import com.wooringpang.userservice.domain.user.dto.UserSearchCondition;
import com.wooringpang.userservice.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {

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
}
