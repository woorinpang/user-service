package com.wooringpang.userservice.domain.user.api.request;

import com.wooringpang.userservice.domain.user.dto.SaveUserParam;
import com.wooringpang.userservice.domain.user.entity.Role;
import com.wooringpang.userservice.domain.user.entity.User;
import com.wooringpang.userservice.domain.user.entity.UserState;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.UUID;

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
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .role(Role.findByCode(this.roleCode))
                .userState(UserState.findByCode(this.userStateCode))
                .build();
    }
}