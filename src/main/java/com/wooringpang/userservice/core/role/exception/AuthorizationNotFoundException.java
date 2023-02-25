package com.wooringpang.userservice.core.role.exception;

import com.wooringpang.userservice.global.exception.EntityNotFoundException;

public class AuthorizationNotFoundException extends EntityNotFoundException {

    public AuthorizationNotFoundException(String message) {
        super(message);
    }
}
