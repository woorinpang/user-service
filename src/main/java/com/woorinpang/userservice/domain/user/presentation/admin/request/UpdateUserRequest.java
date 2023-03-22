package com.woorinpang.userservice.domain.user.presentation.admin.request;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.UserState;
import com.woorinpang.userservice.domain.user.application.dto.request.UpdateUserCommand;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserRequest {
    private String password;
    private String email;
    @Length(max = 1)
    private String name;
    private String roleCode;
    private String userStateCode;

    @Builder
    public UpdateUserRequest(String password, String email, String name, String roleCode, String userStateCode) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.roleCode = roleCode;
        this.userStateCode = userStateCode;
    }

    public void validate() {
        //validate
    }

    public UpdateUserCommand toCommand() {
        return UpdateUserCommand.builder()
                .password(this.password)
                .email(this.email)
                .name(this.name)
                .role(Role.findByCode(this.roleCode))
                .userState(UserState.findByCode(this.userStateCode))
                .build();
    }
}
