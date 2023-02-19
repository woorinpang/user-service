package com.wooringpang.userservice.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
}
