package io.woorinpang.userservice.core.domain.user.domain;

import lombok.Builder;

@Builder
public record JoinUser(
        String email,
        Provider provider,
        String password,
        String name
) {
}
