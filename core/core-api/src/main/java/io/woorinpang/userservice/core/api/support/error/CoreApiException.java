package io.woorinpang.userservice.core.api.support.error;

import lombok.Getter;

@Getter
public class CoreApiException extends RuntimeException {
    private final ErrorType type;

    private final Object data;

    public CoreApiException(ErrorType type) {
        super(type.getMessage());
        this.type = type;
        this.data = null;
    }

    public CoreApiException(ErrorType type, Object data) {
        super(type.getMessage());
        this.type = type;
        this.data = data;
    }
}
