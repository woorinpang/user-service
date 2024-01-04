package io.woorinpang.userservice.support.exception;

import io.woorinpang.userservice.support.exception.dto.ErrorCode;

public class JsonInvalidRequestException extends BusinessException {

    public JsonInvalidRequestException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
