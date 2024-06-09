package io.woorinpang.userservice.admin.support.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import static io.woorinpang.userservice.admin.support.response.AdminApiErrorCode.E400;
import static io.woorinpang.userservice.admin.support.response.AdminApiErrorCode.E500;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum AdminApiErrorType {
    DEFAULT_ERROR(INTERNAL_SERVER_ERROR, E500, "An unexpected error has occurred.", ERROR);

    private final HttpStatus status;

    private final AdminApiErrorCode code;

    private final String message;

    private final LogLevel logLevel;
}
