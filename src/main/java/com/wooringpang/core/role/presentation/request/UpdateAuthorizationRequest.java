package com.wooringpang.core.role.presentation.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateAuthorizationRequest {

    private String authorizationName;
    private String urlPatternValue;
    private String httpMethodCode;
    private Integer sort;

    @Builder
    public UpdateAuthorizationRequest(String authorizationName, String urlPatternValue, String httpMethodCode, Integer sort) {
        this.authorizationName = authorizationName;
        this.urlPatternValue = urlPatternValue;
        this.httpMethodCode = httpMethodCode;
        this.sort = sort;
    }
}
