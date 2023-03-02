package com.wooringpang.core.user.service.param;

import com.wooringpang.core.user.domain.Role;
import com.wooringpang.core.user.domain.UserState;
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
