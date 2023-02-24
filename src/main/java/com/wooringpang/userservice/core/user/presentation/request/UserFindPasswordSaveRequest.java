package com.wooringpang.userservice.core.user.presentation.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFindPasswordSaveRequest {

    private String userName;

    private String email;

    private String mailUrl;

    private String changePasswordUrl;


}
