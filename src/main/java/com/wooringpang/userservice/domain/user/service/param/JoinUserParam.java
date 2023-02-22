package com.wooringpang.userservice.domain.user.service.param;

import com.wooringpang.userservice.domain.user.entity.Role;
import com.wooringpang.userservice.domain.user.entity.User;
import com.wooringpang.userservice.domain.user.entity.UserState;
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
public class JoinUserParam {

    private String name;
    private String email;
    private String password;
    private String provider;
    private String token;
    private boolean isProvider;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.createBuilder()
                .signId(UUID.randomUUID().toString())
                .name(this.name)
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .role(Role.USER)
                .userState(UserState.NORMAL)
                .build();
    }
}
