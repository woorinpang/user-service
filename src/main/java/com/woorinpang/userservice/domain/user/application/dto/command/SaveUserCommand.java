package com.woorinpang.userservice.domain.user.application.dto.command;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.UserState;
import lombok.Builder;

import java.util.Objects;

@Builder
public record SaveUserCommand(
        String username,
        String password,
        String email,
        String name,
        Role role,
        UserState userState
) {
}
