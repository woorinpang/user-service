package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.support.error.CoreDomainException;
import io.woorinpang.userservice.core.domain.support.error.DomainErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserRole{
    ROLE_ANONYMOUS("ROLE_ANONYMOUS", "손님"),
    ROLE_USER("ROLE_USER", "일반 사용자"),
    ROLE_EMPLOYEE("ROLE_EMPLOYEE", "내부 사용자"),
    ROLE_ADMIN("ROLE_ADMIN", "시스템 관리자");

    private final String code;
    private final String description;

    public static UserRole findByCode(String code) {
        return Arrays.stream(UserRole.values())
                .filter(role -> role.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new CoreDomainException(DomainErrorType.DEFAULT_ERROR));
    }
}
