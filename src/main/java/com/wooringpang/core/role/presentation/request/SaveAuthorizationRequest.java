package com.wooringpang.core.role.presentation.request;

import com.wooringpang.core.role.domain.Authorization;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveAuthorizationRequest {

    private String authorizationName;
    private String urlPatternValue;
    private String httpMethodCode;
    private Integer sort;

    public Authorization toEntity() {
        return Authorization.createAuthorization()
                .name(this.authorizationName)
                .urlPatternValue(this.urlPatternValue)
                .httpMethodCode(this.httpMethodCode)
                .sort(this.sort)
                .build();
    }
}
