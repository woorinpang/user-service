package com.woorinpang.userservice.core.user.presentation.request;

import com.woorinpang.common.entity.Role;
import com.woorinpang.userservice.core.user.domain.UserState;
import com.woorinpang.userservice.core.user.service.param.UpdateUserParam;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private String username;
    private String email;
    private String password;
    private String RoleCode;
    private String userStateCode;

    public void validate() {
        //validate
    }

    public UpdateUserParam toParam() {
        return UpdateUserParam.builder()
                .name(this.username)
                .email(this.email)
                .password(this.password)
                .role(Role.findByCode(this.RoleCode))
                .userState(UserState.findByCode(this.userStateCode))
                .build();
    }
}