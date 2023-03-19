package com.woorinpang.userservice.domain.user.presentation.user.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLeaveRequest {

    private String password;

    private String provider;

    private String token;
}
