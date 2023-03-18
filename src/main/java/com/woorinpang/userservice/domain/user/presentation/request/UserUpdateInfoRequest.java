package com.woorinpang.userservice.domain.user.presentation.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateInfoRequest extends UserVerifyRequest {

    private String userName;
    private String email;

    @Builder
    public UserUpdateInfoRequest(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }
}
