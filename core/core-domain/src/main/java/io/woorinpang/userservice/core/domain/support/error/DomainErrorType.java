package io.woorinpang.userservice.core.domain.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

import static io.woorinpang.userservice.core.domain.support.error.DomainErrorCode.*;
import static org.springframework.boot.logging.LogLevel.ERROR;

@Getter
@AllArgsConstructor
public enum DomainErrorType {
    GOOGLE(E1000, "", ERROR),

    USER_ENTITY_NOT_FOUND(E1000, "User not found", ERROR),
    USER_ROLE_ENUM_NOT_FOUND(E1000, "UserRole code not found", ERROR),
    USER_STATE_IS_NOT_NORMAL(E1000, "UserState 가 NORMAL 이 아닙니다.", ERROR),
    PROVIDER_ENUM_NOT_FOUND(E1000, "Provider code not found", ERROR),
    ALREADY_EXISTS_EMAIL(E1000, "동일한 이메일이 존재합니다.", ERROR),
    ALREADY_EXISTS_EMAIL_OTHER_PROVIDER(E1000, "다른 Provider 에서 사용하는 이메일이 이미 존재합니다.", ERROR),

    DEFAULT_ERROR(E9999, "Core domain default error.", ERROR)
    ;

    private final DomainErrorCode code;

    private final String message;

    private final LogLevel logLevel;
}
