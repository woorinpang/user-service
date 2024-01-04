package io.woorinpang.userservice.core.api.user;

import io.woorinpang.userservice.core.api.user.request.ExistsUsernameRequest;
import io.woorinpang.userservice.core.api.user.request.ModifyUserInfoRequest;
import io.woorinpang.userservice.core.api.user.response.FindUserInfoResponse;
import io.woorinpang.userservice.core.api.user.request.JoinUserRequest;
import io.woorinpang.userservice.support.common.json.DefaultSuccessIdResponse;
import io.woorinpang.userservice.core.domain.user.service.UserService;
import io.woorinpang.userservice.core.domain.user.UserTarget;
import io.woorinpang.userservice.support.common.json.JsonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * 내 정보 조회 : 내 정보를 클릭하여 조회한다. -> 로그인 필요함
     */
    @GetMapping("/{userId}")
    public ResponseEntity<JsonResponse> findUserInfo(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(JsonResponse.OK(new FindUserInfoResponse(userService.findUserInfo(userId))));
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/{userId}")
    public ResponseEntity<JsonResponse> modifyUserInfo(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid ModifyUserInfoRequest request
    ) {
        request.validate();

        userService.modifyUserInfo(new UserTarget(userId), request.toUserInfo());
        return ResponseEntity.ok(JsonResponse.OK());
    }

    /**
     * 사용자 회원탈퇴
    */
    @DeleteMapping("/{userId}")
    public ResponseEntity<JsonResponse> userLeave(
            @PathVariable long userId
    ) {
        userService.userLeave(new UserTarget(userId));

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(JsonResponse.OK());
    }
}
