package com.woorinpang.userservice.domain.user.presentation.user.response;

import com.woorinpang.userservice.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponse {
    private Long userId;
    private String username;
    private String email;
    private String name;
    private String roleCode;
    private String userStateCode;

    public UserInfoResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.roleCode = user.getRole().getCode();
        this.userStateCode = user.getUserState().getCode();
    }
}
