package com.woorinpang.userservice.domain.user.exception;


import com.woorinpang.userservice.global.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    private static final String MESSAGE = "UserId=%s은 존재하지 않습니다.";
    public UserNotFoundException(Long userId) {
        super(MESSAGE.formatted(userId));
    }
}
