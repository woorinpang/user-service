package io.woorinpang.userservice.core.api.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import static io.woorinpang.userservice.core.api.support.error.ErrorCode.E500;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@AllArgsConstructor
public enum ErrorType {
    GOOGLE_TOKEN_INVALID(INTERNAL_SERVER_ERROR, E500, "구글 토큰이 유효하지 않습니다.", ERROR),

    PROVIDER_MISMATCH(INTERNAL_SERVER_ERROR, E500, "provider mismatch", ERROR),

    DEFAULT_ERROR(INTERNAL_SERVER_ERROR, E500, "An unexpected error has occurred.", ERROR);

    private final HttpStatus status;

    private final ErrorCode code;

    private final String message;

    private final LogLevel logLevel;
}
