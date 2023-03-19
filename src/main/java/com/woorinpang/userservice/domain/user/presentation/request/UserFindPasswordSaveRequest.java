package com.woorinpang.userservice.domain.user.presentation.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFindPasswordSaveRequest {

    private String userName;

    private String email;

    private String mainUrl;

    private String changePasswordUrl;

}
