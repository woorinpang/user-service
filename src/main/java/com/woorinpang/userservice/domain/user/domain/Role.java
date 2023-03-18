package com.woorinpang.userservice.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {
    //스프링 시큐리티에서는 권한 코드에 항상 ROLE_ 이 앞에 있어야 한다.
    ANONYMOUS("ROLE_ANONYMOUS", "손님"),
    USER("ROLE_USER", "일반 사용자"),
    EMPLOYEE("ROLE_EMPLOYEE", "내부 사용자"),
    ADMIN("ROLE_ADMIN", "시스템 관리자");

    private final String code;
    private final String description;

    public static Role findByCode(String code) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "은 없는 권한코드입니다."));
    }
}
