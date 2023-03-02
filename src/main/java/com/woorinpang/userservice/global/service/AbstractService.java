package com.woorinpang.userservice.global.service;

import com.woorinpang.userservice.global.util.MessageUtil;
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
