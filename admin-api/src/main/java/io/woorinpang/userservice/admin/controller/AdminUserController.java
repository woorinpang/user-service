package io.woorinpang.userservice.admin.controller;

import io.woorinpang.userservice.admin.domain.user.AdminUserService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity findUsers() {
        long users = adminUserService.findUsers(2L);
        return ResponseEntity.ok().body(users);
    }
}
