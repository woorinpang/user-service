package io.woorinpang.userservice.support.exception;

import io.woorinpang.userservice.support.exception.dto.ErrorCode;

public class EnumTypeMismatchException extends BusinessException {

    public EnumTypeMismatchException(String message) {
        super(ErrorCode.INVALID_TYPE_VALUE, message);
    }
}
