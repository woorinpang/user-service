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

    USER_NOT_FOUND(E1000, "User not found", ERROR),

    DEFAULT_ERROR(E9999, "Core domain default error.", ERROR)
    ;

    private final DomainErrorCode code;

    private final String message;

    private final LogLevel logLevel;
}
