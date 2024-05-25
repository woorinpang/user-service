package io.woorinpang.userservice.core.domain.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

import static io.woorinpang.userservice.core.domain.support.error.DomainErrorCode.*;
import static org.springframework.boot.logging.LogLevel.ERROR;

@Getter
@AllArgsConstructor
public enum DomainErrorType {
    DEFAULT_ERROR(E1000, "User not found.", ERROR);

    private final DomainErrorCode code;

    private final String message;

    private final LogLevel logLevel;
}
