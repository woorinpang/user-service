package io.woorinpang.userservice.core.domain.user.domain;

import lombok.Builder;

@Builder
public record ModifyUser(
        String name
) {
}
