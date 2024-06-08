package io.woorinpang.userservice.core.domain.user.domain;

import lombok.Builder;

@Builder
public record UserLoginLogCommand(
        Long siteId,
        String email,
        Provider provider,
        String remoteIp,
        boolean success,
        String failContent
) {
}
