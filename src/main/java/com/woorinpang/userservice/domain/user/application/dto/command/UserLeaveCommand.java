package com.woorinpang.userservice.domain.user.application.dto.command;

import com.woorinpang.userservice.global.common.entity.Provider;
import lombok.Builder;

@Builder
public record UserLeaveCommand(
        String password,
        Provider provider,
        String token
) {
}
