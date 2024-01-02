package io.woorinpang.userservice.core.api.user;

import com.woorinpang.userservice.domain.user.presentation.admin.request.SaveUserRequest;
import com.woorinpang.userservice.domain.user.presentation.admin.request.UpdateUserRequest;
import com.woorinpang.userservice.domain.user.presentation.admin.response.FindUserResponse;
import com.woorinpang.userservice.domain.user.presentation.admin.response.SaveUserResponse;
import com.woorinpang.userservice.domain.user.presentation.admin.response.UpdateUserResponse;
import io.woorinpang.userservice.core.domain.user.UserService;
import io.woorinpang.userservice.core.domain.user.dto.UserSearchParam;
import io.woorinpang.userservice.support.common.json.JsonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    /**
     * 사용자 목록조회
     */
    @GetMapping
    public ResponseEntity<JsonResponse> findPageUser(UserSearchParam param,
                                                     @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(userService.findPageUser(param, pageable)));
    }

    /**
     * 사용자 단건조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<JsonResponse> findUser(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(new FindUserResponse(userService.findUser(userId))));
    }

    /**
     * 사용자 저장
     */
    @PostMapping
    public ResponseEntity<JsonResponse> saveUser(@RequestBody @Valid SaveUserRequest request) {
        //validate
        request.validate();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(JsonResponse.CREATED(new SaveUserResponse(userService.saveUser(request.toCommand()))));
    }

    /**
     * 사용자 수정
     */
    @PutMapping("/{userId}")
    public ResponseEntity<JsonResponse> updateUser(@PathVariable("userId") Long userId, @RequestBody @Valid UpdateUserRequest request) {
        //validate
        userService.updateUser(userId, request.toCommand());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonResponse.OK(new UpdateUserResponse(userService.findUser(userId).getId())));
    }

    /**
     * 사용자 삭제
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<JsonResponse> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(JsonResponse.NO_CONTENT());
    }
}
