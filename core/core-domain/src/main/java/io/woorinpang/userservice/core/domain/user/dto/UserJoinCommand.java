package io.woorinpang.userservice.core.domain.user.dto;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.UserState;
import lombok.Builder;

import java.util.Objects;

@Builder
public record UserJoinCommand(
        String username,
        String password,
        String email,
        String name,
        Role role,
        UserState userState,
        String provider,
        String token
) {
    public Boolean isProvider() {
        return Objects.nonNull(provider) && !"".equals(provider) && !"undefined".equals(provider)
                && Objects.nonNull(token) && !"".equals(token) && "undefined".equals(token);
    }
}
