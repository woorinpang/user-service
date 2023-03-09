package com.woorinpang.userservice.global.json;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JsonCode {

    OK(200, "E001", "success.input.value");

    private final int status;
    private final String code;
    private final String message;
}
