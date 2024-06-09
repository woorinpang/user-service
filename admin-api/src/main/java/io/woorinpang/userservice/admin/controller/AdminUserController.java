package io.woorinpang.userservice.admin.controller;

import io.woorinpang.userservice.admin.domain.user.AdminUserSearchCondition;
import io.woorinpang.userservice.admin.domain.user.AdminUserService;
import io.woorinpang.userservice.admin.support.response.AdminApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<AdminApiResponse<Page<AdminFindUsersResponse>>> findUsers(
            AdminUserSearchCondition condition,
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        Page<AdminFindUsersResponse> response =
                adminUserService.findUsers(condition, pageable).map(AdminFindUsersResponse::new);
        return ResponseEntity.ok().body(AdminApiResponse.success(response));
    }
}
