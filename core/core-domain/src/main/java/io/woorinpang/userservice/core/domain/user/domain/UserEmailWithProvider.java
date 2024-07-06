package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.storage.db.core.user.Provider;

public record UserEmailWithProvider(
        String email,
        Provider provider
) {
}
