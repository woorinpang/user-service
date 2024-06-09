package io.woorinpang.userservice.admin.controller;

import io.woorinpang.userservice.admin.controller.request.AdminSaveUserRequest;
import io.woorinpang.userservice.admin.controller.response.AdminFindUsersResponse;
import io.woorinpang.userservice.admin.domain.user.AdminUserSearchCondition;
import io.woorinpang.userservice.admin.domain.user.AdminUserService;
import io.woorinpang.userservice.admin.support.response.AdminApiResponse;
import io.woorinpang.userservice.admin.support.response.AdminDefaultIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity findUsers(
            AdminUserSearchCondition condition,
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        Page<AdminFindUsersResponse> response =
                adminUserService.findUsers(condition, pageable).map(AdminFindUsersResponse::new);
        return ResponseEntity.ok().body(AdminApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity saveUser(
            @RequestBody AdminSaveUserRequest request
    ) {
        return ResponseEntity.ok().body(AdminApiResponse.success(new AdminDefaultIdResponse(1L)));
    }
}
