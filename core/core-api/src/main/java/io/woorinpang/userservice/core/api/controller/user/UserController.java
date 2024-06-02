package io.woorinpang.userservice.core.api.controller.user;

import io.woorinpang.userservice.core.api.controller.user.request.ModifyUserRequest;
import io.woorinpang.userservice.core.api.controller.user.response.FindUserResponse;
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
    public ResponseEntity<ApiResponse<FindUserResponse>> findUser(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(ApiResponse.success(new FindUserResponse(userService.findUserInfo(new UserTarget(userId)))));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> modifyUser(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid ModifyUserRequest request
    ) {
        request.validate();

        userService.modifyUser(new UserTarget(userId), request.getName());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> leaveUser(
            @PathVariable long userId
    ) {
        userService.leaveUser(new UserTarget(userId));
        return ResponseEntity.ok(ApiResponse.success());
    }
}
