package com.woorinpang.userservice.domain.user.presentation.admin.request;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.UserState;
import com.woorinpang.userservice.domain.user.application.dto.request.UpdateUserCommand;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserRequest {
    private String username;
    private String email;
    private String password;
    private String RoleCode;
    private String userStateCode;

    public void validate() {
        //validate
    }

    public UpdateUserCommand toCommand() {
        return UpdateUserCommand.builder()
                .name(this.username)
                .email(this.email)
                .password(this.password)
                .role(Role.findByCode(this.RoleCode))
                .userState(UserState.findByCode(this.userStateCode))
                .build();
    }
}
