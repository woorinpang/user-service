package com.woorinpang.userservice.domain.user.application.dto.command;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.UserState;
import lombok.*;

@Builder
public record UpdateUserCommand(
        String email,
        String password,
        String name,
        Role role,
        UserState userState
) {
}
