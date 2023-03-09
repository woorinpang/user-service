package com.woorinpang.userservice.core.user.application.param;

import com.woorinpang.userservice.core.user.domain.Role;
import com.woorinpang.userservice.core.user.domain.UserState;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateUserParam {

    private String email;
    private String password;
    private String name;
    private Role role;
    private UserState userState;

    public void encodePassword(String password) {
        this.password = password;
    }
}
