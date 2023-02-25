package com.wooringpang.userservice.global.service;

import com.wooringpang.userservice.global.util.MessageUtil;
import jakarta.annotation.Resource;

public abstract class AbstractService {

    @Resource(name = "messageUtil")
    private MessageUtil messageUtil;

    protected String getMessage(String code) {
        return messageUtil.getMessage(code);
    }

    protected String getMessage(String code, Object[] args) {
        return messageUtil.getMessage(code, args);
    }
}
