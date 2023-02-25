package com.wooringpang.userservice.core.role.presentation.request;

import com.wooringpang.userservice.core.role.domain.RoleAuthorization;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveRoleAuthorizationRequest {

    private String roleCode;
    private Long authorizationId;

    public RoleAuthorization toEntity() {
        return RoleAuthorization.createRoleAuthorization()
                .roleCode(this.roleCode)
                .authorizationId(this.authorizationId)
                .build();
    }
}
