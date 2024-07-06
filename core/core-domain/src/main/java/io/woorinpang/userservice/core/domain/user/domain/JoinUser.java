package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.storage.db.core.user.Provider;
import lombok.Builder;

@Builder
public record JoinUser(
        String email,
        Provider provider,
        String password,
        String name
) {
}
