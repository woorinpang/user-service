package com.wooringpang.userservice.core.user.presentation.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialUserRequest {

    /* 공급자 */
    private String provider;
    /* 토큰*/
    private String token;
}
