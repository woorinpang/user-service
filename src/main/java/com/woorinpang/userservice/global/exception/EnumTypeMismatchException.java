package com.woorinpang.userservice.global.exception;

public class EnumTypeMismatchException extends BusinessException {

    public EnumTypeMismatchException(String message) {
        super(ErrorCode.INVALID_TYPE_VALUE, message);
    }
}
