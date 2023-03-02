package com.woorinpang.userservice.core.user.service.param;

import com.woorinpang.common.entity.Role;
import com.woorinpang.userservice.core.user.domain.UserState;
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
