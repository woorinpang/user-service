package io.woorinpang.userservice.core.db.user.dto;

import io.woorinpang.userservice.core.db.user.UserRole;
import io.woorinpang.userservice.core.db.user.UserState;
import lombok.*;

@Builder
public record ModifyUserCommand(
        String email,
        String password,
        String name,
        UserRole role,
        UserState userState
) {
}
