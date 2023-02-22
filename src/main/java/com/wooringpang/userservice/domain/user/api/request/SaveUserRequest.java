package com.wooringpang.userservice.domain.user.api.request;

import com.wooringpang.userservice.domain.user.service.param.SaveUserParam;
import com.wooringpang.userservice.domain.user.entity.Role;
import com.wooringpang.userservice.domain.user.entity.UserState;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveUserRequest {

    private String username;
    private String email;
    private String password;
    private String roleCode;
    private String userStateCode;

    public void validate() {
        //validate
    }

    public SaveUserParam toParam() {
        return SaveUserParam.builder()
                .name(this.username)
                .email(this.email)
                .password(this.password)
                .role(Role.findByCode(this.roleCode))
                .userState(UserState.findByCode(this.userStateCode))
                .build();
    }
}
