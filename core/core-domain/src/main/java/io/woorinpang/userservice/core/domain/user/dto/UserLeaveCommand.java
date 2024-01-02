package io.woorinpang.userservice.core.domain.user.dto;

import com.woorinpang.userservice.global.common.entity.Provider;
import lombok.Builder;

@Builder
public record UserLeaveCommand(
        String password,
        Provider provider,
        String token
) {
}
