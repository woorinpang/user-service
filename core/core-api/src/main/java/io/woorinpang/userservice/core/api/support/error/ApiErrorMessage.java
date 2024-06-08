package io.woorinpang.userservice.core.api.support.error;

import lombok.Getter;

@Getter
public class ApiErrorMessage {
    private final String code;

    private final String message;

    private final Object data;

    public ApiErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public ApiErrorMessage(ApiErrorType errorType) {
        this.code = errorType.getCode().name();
        this.message = errorType.getMessage();
        this.data = null;
    }

    public ApiErrorMessage(ApiErrorType errorType, Object data) {
        this.code = errorType.getCode().name();
        this.message = errorType.getMessage();
        this.data = data;
    }
}
