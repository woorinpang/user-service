package io.woorinpang.userservice.core.api.support.error;

import lombok.Getter;

@Getter
public class CoreApiException extends RuntimeException {
    private final ApiErrorType type;

    private final Object data;

    public CoreApiException(ApiErrorType type) {
        super(type.getMessage());
        this.type = type;
        this.data = null;
    }

    public CoreApiException(ApiErrorType type, Object data) {
        super(type.getMessage());
        this.type = type;
        this.data = data;
    }
}
