package io.woorinpang.userservice.admin.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AdminUserRole {
    ROLE_ANONYMOUS("ROLE_ANONYMOUS", "손님"),
    ROLE_USER("ROLE_USER", "일반 사용자"),
    ROLE_EMPLOYEE("ROLE_EMPLOYEE", "내부 사용자"),
    ROLE_ADMIN("ROLE_ADMIN", "시스템 관리자");

    private final String code;
    private final String description;

    public static AdminUserRole findByCode(String code) {
        return Arrays.stream(AdminUserRole.values())
                .filter(role -> role.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("UserState code not found"));
    }
}
