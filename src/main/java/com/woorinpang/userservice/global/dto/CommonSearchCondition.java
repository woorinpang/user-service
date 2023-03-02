package com.woorinpang.userservice.global.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CommonSearchCondition {

    private KeywordType searchKeywordType;
    private String searchKeyword;


    @Getter
    @AllArgsConstructor
    public enum KeywordType {
        CODE("code", "코드"),
        NAME("name", "이름"),
        CONTENT("content", "내용"),
        EMAIL("email", "이메일")
        ;

        private final String code;
        private final String description;
    }
}
