package io.woorinpang.userservice.core.domain.user.dto;

import lombok.Builder;

@Builder
public record UserUpdateInfoCommand(
        String name,
        String email
) {
}
