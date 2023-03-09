package com.woorinpang.userservice.core.user.application.dto.request;

import com.woorinpang.userservice.core.user.domain.Role;
import com.woorinpang.userservice.core.user.domain.UserState;
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
}
