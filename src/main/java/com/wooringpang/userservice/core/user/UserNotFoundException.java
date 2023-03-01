package com.wooringpang.userservice.core.user;

import com.wooringpang.userservice.global.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
