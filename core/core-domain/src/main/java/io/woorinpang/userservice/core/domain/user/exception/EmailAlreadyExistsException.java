package io.woorinpang.userservice.core.domain.user.exception;

import com.woorinpang.userservice.global.exception.InvalidValueException;

public class EmailAlreadyExistsException extends InvalidValueException {
    private static final String MESSAGE = "Email=%s은 이미 존재합니다.";

    public EmailAlreadyExistsException(String value) {
        super(MESSAGE.formatted(value));
    }
}
