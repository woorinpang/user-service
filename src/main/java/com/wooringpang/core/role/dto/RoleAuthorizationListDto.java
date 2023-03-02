package com.wooringpang.core.role.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleAuthorizationListDto {

    private String roleCode;
    private Long authorizationId;
    private String authorizationName;
    private String urlPatternValue;
    private String httpMethodCode;
    private Integer sort;
    private Boolean createdAt;

    @QueryProjection
    @Builder
    public RoleAuthorizationListDto(String roleCode, Long authorizationId, String authorizationName, String urlPatternValue, String httpMethodCode, Integer sort, Boolean createdAt) {
        this.roleCode = roleCode;
        this.authorizationId = authorizationId;
        this.authorizationName = authorizationName;
        this.urlPatternValue = urlPatternValue;
        this.httpMethodCode = httpMethodCode;
        this.sort = sort;
        this.createdAt = createdAt;
    }
}
