package com.woorinpang.userservice.domain.user.application.dto.command;

import lombok.Builder;

@Builder
public record UserUpdateInfoCommand(
        String name,
        String email
) {
}
