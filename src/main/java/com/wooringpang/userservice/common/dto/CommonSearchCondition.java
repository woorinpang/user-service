package com.wooringpang.userservice.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonSearchCondition {

    private KeywordType searchKeywordType;
    private String searchKeyword;
}
