package com.woorinpang.userservice.domain.user.presentation.request;

import com.woorinpang.userservice.domain.user.presentation.user.request.UserLeaveRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPasswordUpdateRequest extends UserLeaveRequest {

    private String newPassword;
}
