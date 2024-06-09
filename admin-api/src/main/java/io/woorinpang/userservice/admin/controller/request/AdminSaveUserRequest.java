package io.woorinpang.userservice.admin.controller.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminSaveUserRequest {
    private String email;

    private String password;

    private String name;

    private String userRole;

    private String userState;
}
