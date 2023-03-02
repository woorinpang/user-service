package com.woorinpang.userservice.core.role.exception;

import com.woorinpang.userservice.global.exception.EntityNotFoundException;

public class AuthorizationNotFoundException extends EntityNotFoundException {

    public AuthorizationNotFoundException(String message) {
        super(message);
    }
}
