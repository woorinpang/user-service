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
    private String username;
    private String password;
    private String email;
    private String name;
    private Role role;
    private UserState userState;

    public User toEntity() {
        return User.createBuilder()
                .username(this.username)
                .password(this.password)
                .email(this.email)
                .name(this.name)
                .role(this.role)
                .userState(this.userState)
                .build();
    }
}
