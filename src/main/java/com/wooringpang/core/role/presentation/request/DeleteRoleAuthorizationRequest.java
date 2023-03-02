package com.wooringpang.core.role.presentation.request;

import com.wooringpang.core.role.domain.RoleAuthorization;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteRoleAuthorizationRequest {

    private String roleCode;
    private Long authorizationId;

    @Builder
    public DeleteRoleAuthorizationRequest(String roleCode, Long authorizationId) {
        this.roleCode = roleCode;
        this.authorizationId = authorizationId;
    }

    public RoleAuthorization toEntity() {
        return RoleAuthorization.createRoleAuthorization()
                .roleCode(this.roleCode)
                .authorizationId(this.authorizationId)
                .build();
    }
}
