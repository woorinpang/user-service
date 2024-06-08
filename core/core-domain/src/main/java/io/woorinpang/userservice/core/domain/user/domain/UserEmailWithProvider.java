package io.woorinpang.userservice.core.domain.user.domain;

public record UserEmailWithProvider(
        String email,
        Provider provider
) {
}
