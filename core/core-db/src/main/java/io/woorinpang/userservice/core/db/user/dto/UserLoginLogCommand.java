package io.woorinpang.userservice.core.db.user.dto;

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
