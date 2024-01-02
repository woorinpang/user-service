package io.woorinpang.userservice.core.domain.user.dto;

import lombok.Builder;

@Builder
public record SocialUserCommand(
        String id,
        String email,
        String name
) {
}
