package com.woorinpang.userservice.domain.user.presentation.admin.request;

import io.woorinpang.userservice.core.domain.user.dto.command.SaveUserCommand;
import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.UserState;
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

    public SaveUserCommand toCommand() {
        return SaveUserCommand.builder()
                .username(this.username)
                .password(this.password)
                .email(this.email)
                .name(this.name)
                .role(Role.findByCode(this.roleCode))
                .userState(UserState.findByCode(this.userStateCode))
                .build();
    }
}
