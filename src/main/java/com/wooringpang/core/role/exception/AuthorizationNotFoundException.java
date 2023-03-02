package com.wooringpang.core.role.exception;

import com.wooringpang.global.exception.EntityNotFoundException;

public class AuthorizationNotFoundException extends EntityNotFoundException {

    public AuthorizationNotFoundException(String message) {
        super(message);
    }
}
