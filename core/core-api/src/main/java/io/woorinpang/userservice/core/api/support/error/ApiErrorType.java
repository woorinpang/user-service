package io.woorinpang.userservice.core.api.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import static io.woorinpang.userservice.core.api.support.error.ApiErrorCode.E400;
import static io.woorinpang.userservice.core.api.support.error.ApiErrorCode.E500;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ApiErrorType {
    GOOGLE_TOKEN_INVALID(UNAUTHORIZED, E400, "구글 토큰이 유효하지 않습니다.", ERROR),
    KAKAO_TOKEN_INVALID(UNAUTHORIZED, E400, "카카오 토큰이 유효하지 않습니다.", ERROR),
    NAVER_TOKEN_INVALID(UNAUTHORIZED, E400, "네이버 토큰이 유효하지 않습니다.", ERROR),

    PROVIDER_MISMATCH(UNAUTHORIZED, E400, "provider mismatch", ERROR),

    INVALID_PARAMETER(BAD_REQUEST, E400, "Bad Request.", ERROR),
    DEFAULT_ERROR(INTERNAL_SERVER_ERROR, E500, "An unexpected error has occurred.", ERROR);

    private final HttpStatus status;

    private final ApiErrorCode code;

    private final String message;

    private final LogLevel logLevel;
}
