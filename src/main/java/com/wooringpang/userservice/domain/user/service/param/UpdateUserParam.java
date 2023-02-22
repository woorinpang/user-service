package com.wooringpang.userservice.domain.user.service.param;

import com.wooringpang.userservice.domain.user.entity.Role;
import com.wooringpang.userservice.domain.user.entity.UserState;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateUserParam {

    private String name;
    private String email;
    private String password;
    private Role role;
    private UserState userState;

    public void encodePassword(String password) {
        this.password = password;
    }
}