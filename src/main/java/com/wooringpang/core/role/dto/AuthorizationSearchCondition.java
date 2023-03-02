package com.wooringpang.core.role.dto;

import com.wooringpang.global.dto.CommonSearchCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationSearchCondition extends CommonSearchCondition {

    private String searchUrlPatternValue;
    private String searchHttpMethodCode;
}
