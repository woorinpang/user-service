package com.wooringpang.userservice.core.role.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizationListDto implements Serializable {

    private static final long serialVersionUID = 5538951453253303141L;

    private Long authorizationId;

    private String authorizationName;
    private String urlPatternValue;
    private String httpMethodCode;

    private Integer sort;

    @QueryProjection
    public AuthorizationListDto(Long authorizationId, String authorizationName, String urlPatternValue, String httpMethodCode, Integer sort) {
        this.authorizationId = authorizationId;
        this.authorizationName = authorizationName;
        this.urlPatternValue = urlPatternValue;
        this.httpMethodCode = httpMethodCode;
        this.sort = sort;
    }
}
