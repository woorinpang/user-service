package io.woorinpang.userservice.support.exception;


import io.woorinpang.userservice.support.exception.dto.ErrorCode;

public class BusinessMessageException extends BusinessException {

    public BusinessMessageException(String customMessage) {
        super(ErrorCode.BUSINESS_CUSTOM_MESSAGE, customMessage);
    }
}
