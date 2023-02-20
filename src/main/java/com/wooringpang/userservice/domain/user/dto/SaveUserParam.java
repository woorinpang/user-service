package com.wooringpang.userservice.domain.user.dto;

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
public class SaveUserParam {

    private String username;
    private String email;
    private String password;
    private Role role;
    private UserState userState;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.createBuilder()
                .signId(UUID.randomUUID().toString())
                .username(this.username)
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .role(this.role)
                .userState(this.userState)
                .build();
    }
}
