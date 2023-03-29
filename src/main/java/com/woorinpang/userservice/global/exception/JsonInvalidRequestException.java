package com.woorinpang.userservice.global.exception;

import com.woorinpang.userservice.global.exception.dto.ErrorCode;

public class JsonInvalidRequestException extends BusinessException {

    public JsonInvalidRequestException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
