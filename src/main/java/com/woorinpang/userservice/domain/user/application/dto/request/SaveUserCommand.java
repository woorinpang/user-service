package com.woorinpang.userservice.domain.user.application.dto.request;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
import lombok.Builder;

@Builder
public record SaveUserCommand(
        String username,
        String password,
        String email,
        String name,
        Role role,
        UserState userState
) {
    public SaveUserCommand(String username, String password, String email, String name, Role role, UserState userState) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
        this.userState = userState;
    }

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
