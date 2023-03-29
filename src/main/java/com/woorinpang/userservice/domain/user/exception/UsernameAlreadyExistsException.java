package com.woorinpang.userservice.domain.user.exception;

import com.woorinpang.userservice.global.exception.BusinessException;
import com.woorinpang.userservice.global.exception.InvalidValueException;

public class UsernameAlreadyExistsException extends InvalidValueException {

    private static final String MESSAGE = "Username=%s은 이미 존재합니다.";

    public UsernameAlreadyExistsException(String value) {
        super(MESSAGE.formatted(value));
    }
}
