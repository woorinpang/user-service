package com.woorinpang.userservice.core.user.exception;


import com.woorinpang.common.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
