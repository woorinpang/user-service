package io.woorinpang.userservice.storage.core.db.user.dto;

import lombok.Builder;

@Builder
public record UserJoinCommand(
        String username,
        String password,
        String email,
        String name
) {
}
