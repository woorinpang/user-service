package io.woorinpang.userservice.core.api.controller.user;

import io.woorinpang.userservice.core.api.controller.user.request.ModifyUserInfoRequest;
import io.woorinpang.userservice.core.api.controller.user.response.FindUserInfoResponse;
import io.woorinpang.userservice.core.api.support.response.ApiResponse;
import io.woorinpang.userservice.core.domain.user.application.UserService;
import io.woorinpang.userservice.core.domain.user.domain.UserTarget;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<FindUserInfoResponse>> findUserInfo(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(ApiResponse.success(new FindUserInfoResponse(userService.findUserInfo(userId))));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> modifyUserInfo(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid ModifyUserInfoRequest request
    ) {
        request.validate();

        userService.modifyUserInfo(new UserTarget(userId), request.toUserInfo());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> userLeave(
            @PathVariable long userId
    ) {
        userService.userLeave(new UserTarget(userId));

        return ResponseEntity.ok(ApiResponse.success());
    }
}
