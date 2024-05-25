package io.woorinpang.userservice.core.domain.user.domain;

import lombok.Builder;

@Builder
public record JoinUser(
        String email,
        String password,
        String name
) {
}
