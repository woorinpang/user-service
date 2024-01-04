package io.woorinpang.userservice.core.db.user.dto;

import lombok.Builder;

@Builder
public record ModifyUserCommand(
        String email,
        String name
) {
}
