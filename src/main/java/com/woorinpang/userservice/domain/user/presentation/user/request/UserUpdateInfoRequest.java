package com.woorinpang.userservice.domain.user.presentation.user.request;

import com.woorinpang.userservice.domain.user.application.dto.command.UserUpdateInfoCommand;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserUpdateInfoRequest extends UserLeaveRequest {

    private String name;
    private String email;

    public void validate() {
        //validate
    }

    public UserUpdateInfoCommand toCommand() {
        return UserUpdateInfoCommand.builder()
                .name(this.name)
                .email(this.email)
                .build();
    }
}
