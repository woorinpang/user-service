package io.woorinpang.userservice.storage.core.db.user.dto;

import lombok.Builder;

@Builder
public record ModifyUserCommand(
        String email,
        String name
) {
}
