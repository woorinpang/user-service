package com.woorinpang.userservice.core.role.dto;

import com.woorinpang.common.dto.CommonSearchCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleAuthorizationSearchCondition extends CommonSearchCondition {

    private String roleCode;
    private String searchUrlPatternValue;
    private String searchHttpMethodCode;
}
