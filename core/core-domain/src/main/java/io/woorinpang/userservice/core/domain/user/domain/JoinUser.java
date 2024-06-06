package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.enums.user.Provider;
import lombok.Builder;

@Builder
public record JoinUser(
        String email,
        String password,
        String name,
        Provider provider
) {
}
