package com.woorinpang.userservice.domain.user.presentation.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class SaveUserRequest {

    private String username;
    private String password;
    private String email;
    private String name;
    private String roleCode;
    private String userStateCode;

    public void validate() {
        //validate
    }

    @Builder
    public SaveUserRequest(String username, String password, String email, String name, String roleCode, String userStateCode) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.roleCode = roleCode;
        this.userStateCode = userStateCode;
    }
}
