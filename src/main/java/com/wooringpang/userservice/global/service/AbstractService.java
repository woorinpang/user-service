package com.wooringpang.userservice.global.service;

import com.wooringpang.userservice.global.util.MessageUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {

    @Autowired
    private MessageUtil messageUtil;

    protected String getMessage(String code) {
        return messageUtil.getMessage(code);
    }

    protected String getMessage(String code, Object[] args) {
        return messageUtil.getMessage(code, args);
    }
}
