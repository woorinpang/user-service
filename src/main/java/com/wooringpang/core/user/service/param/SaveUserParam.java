package com.wooringpang.core.user.service.param;

import com.wooringpang.core.user.domain.Role;
import com.wooringpang.core.user.domain.User;
import com.wooringpang.core.user.domain.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveUserParam {

    private String name;
    private String email;
    private String password;
    private Role role;
    private UserState userState;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.createBuilder()
                .signId(UUID.randomUUID().toString())
                .name(this.name)
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .role(this.role)
                .userState(this.userState)
                .build();
    }
}
