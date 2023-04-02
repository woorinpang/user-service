package com.woorinpang.userservice.global.common.exception;

import com.woorinpang.userservice.global.exception.EnumTypeMismatchException;

public class ProviderTypeMismatchException extends EnumTypeMismatchException {

    public static final String MESSAGE = "Provider = %s는 없는 타입닙니다.";

    public ProviderTypeMismatchException(String code) {
        super(MESSAGE.formatted(code));
    }
}
