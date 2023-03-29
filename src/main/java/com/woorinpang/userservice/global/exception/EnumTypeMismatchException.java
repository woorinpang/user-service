package com.woorinpang.userservice.global.exception;

import com.woorinpang.userservice.global.exception.dto.ErrorCode;

public class EnumTypeMismatchException extends BusinessException {

    public EnumTypeMismatchException(String message) {
        super(ErrorCode.INVALID_TYPE_VALUE, message);
    }
}
