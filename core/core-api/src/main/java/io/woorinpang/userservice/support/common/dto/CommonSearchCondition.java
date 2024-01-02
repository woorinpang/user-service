package io.woorinpang.userservice.support.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CommonSearchCondition {

    private String searchKeyword;

    public CommonSearchCondition(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
}
