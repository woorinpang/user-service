package com.wooringpang.userservice.core.user.presentation.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserVerifyRequest {

    private String password;

    private String provider;

    private String token;
}
