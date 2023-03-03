package com.woorinpang.userservice.core.role.exception;


import com.woorinpang.common.exception.EntityNotFoundException;

public class AuthorizationNotFoundException extends EntityNotFoundException {

    public AuthorizationNotFoundException(String message) {
        super(message);
    }
}
