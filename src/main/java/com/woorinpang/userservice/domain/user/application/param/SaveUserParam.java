package com.woorinpang.userservice.domain.user.application.param;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
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
                .username(UUID.randomUUID().toString())
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .name(this.name)
                .role(this.role)
                .userState(this.userState)
                .build();
    }
}
