package io.woorinpang.userservice.core.domain.user.domain;

import lombok.Builder;

@Builder
public record UserLoginLogCommand(
        Long siteId,
        String username,
        String remoteIp,
        boolean success,
        String failContent
) {
}
