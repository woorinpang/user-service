package com.wooringpang.userservice.core.user.presentation.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialUserResponse {

    private String id;
    private String email;
    private String name;

    @Builder
    public SocialUserResponse(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
