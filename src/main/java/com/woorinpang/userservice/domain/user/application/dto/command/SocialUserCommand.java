package com.woorinpang.userservice.domain.user.application.dto.command;

import lombok.Builder;

@Builder
public record SocialUserCommand(
        String id,
        String email,
        String name
) {
}
