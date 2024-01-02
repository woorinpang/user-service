package io.woorinpang.userservice.support.common.cotroller;

import com.woorinpang.userservice.global.util.MessageUtil;
import jakarta.annotation.Resource;

public abstract class CommonController {
    @Resource
    private MessageUtil messageUtil;

    protected String getMessage(String code) {
        return messageUtil.getMessage(code);
    }

    protected String getMessage(String code, Object[] args) {
        return messageUtil.getMessage(code, args);
    }
}
