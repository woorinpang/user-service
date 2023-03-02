package com.wooringpang.global.exception;

import com.wooringpang.global.exception.dto.ErrorCode;

public class BusinessMessageException extends BusinessException {

    public BusinessMessageException(String customMessage) {
        super(ErrorCode.BUSINESS_CUSTOM_MESSAGE, customMessage);
    }
}
