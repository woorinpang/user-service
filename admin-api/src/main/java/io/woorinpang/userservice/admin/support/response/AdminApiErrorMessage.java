package io.woorinpang.userservice.admin.support.response;

import lombok.Getter;

@Getter
public class AdminApiErrorMessage {
    private final String code;

    private final String message;

    private final Object data;

    public AdminApiErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public AdminApiErrorMessage(AdminApiErrorType errorType) {
        this.code = errorType.getCode().name();
        this.message = errorType.getMessage();
        this.data = null;
    }

    public AdminApiErrorMessage(AdminApiErrorType errorType, Object data) {
        this.code = errorType.getCode().name();
        this.message = errorType.getMessage();
        this.data = data;
    }
}
