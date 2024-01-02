package io.woorinpang.userservice.core.domain.user.exception;

import com.woorinpang.userservice.global.exception.InvalidValueException;

public class PasswordNotMatchException extends InvalidValueException {

    private static final String MESSAGE = "패스워드가 일치하지 않습니다.";

    public PasswordNotMatchException() {
        super(MESSAGE);
    }
}
