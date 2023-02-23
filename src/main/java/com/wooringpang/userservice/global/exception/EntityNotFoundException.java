package com.wooringpang.userservice.global.exception;

import com.wooringpang.userservice.global.exception.dto.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
