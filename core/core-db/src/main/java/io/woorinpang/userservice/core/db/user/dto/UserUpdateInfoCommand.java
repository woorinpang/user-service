package io.woorinpang.userservice.core.db.user.dto;

import lombok.Builder;

@Builder
public record UserUpdateInfoCommand(
        String name,
        String email
) {
}
