package com.wooringpang.core.role.presentation.response;

import com.wooringpang.core.role.dto.RoleAuthorizationListDto;
import com.wooringpang.core.role.domain.Authorization;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindAuthorizationResponse {

    private Long authorizationId;
    private String authorizationName;
    private String urlPatternValue;
    private String httpMethodCode;
    private Integer sort;

    private List<RoleAuthorizationListDto> roleAuthorizations;

    public FindAuthorizationResponse(Authorization authorization) {
        this.authorizationId = authorization.getId();
        this.authorizationName = authorization.getName();
        this.urlPatternValue = authorization.getUrlPatternValue();
        this.httpMethodCode = authorization.getHttpMethodCode();
        this.sort = authorization.getSort();
        if (authorization.getRoleAuthorizations() != null) {
            this.roleAuthorizations = authorization.getRoleAuthorizations().stream()
                    .map(roleAuthorization -> RoleAuthorizationListDto.builder()
                            .roleCode(roleAuthorization.getRoleAuthorizationId().getRoleCode())
                            .authorizationId(roleAuthorization.getRoleAuthorizationId().getAuthorizationId())
                            .build()
                    )
                    .collect(Collectors.toList());
        }
    }
}
