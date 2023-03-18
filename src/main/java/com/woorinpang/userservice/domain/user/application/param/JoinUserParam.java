package com.woorinpang.userservice.domain.user.application.param;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinUserParam {

    private String email;
    private String password;
    private String name;
    private String provider;
    private String token;
    private boolean isProvider;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.createBuilder()
                .username(this.email)
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .name(this.name)
                .role(Role.USER)
                .userState(UserState.NORMAL)
                .build();
    }
}
