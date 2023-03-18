package com.woorinpang.userservice.domain.user.presentation.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFindPasswordUpdateRequest {

    private String tokenValue;

    private String password;
}
