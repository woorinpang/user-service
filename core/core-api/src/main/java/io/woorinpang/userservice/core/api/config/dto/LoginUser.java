package io.woorinpang.userservice.core.api.config.dto;

import io.woorinpang.userservice.core.domain.user.domain.FindUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginUser {
    private long userId;
    private String email;
    private String name;
    private String role;

    public LoginUser(FindUser user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getUserRole().getCode();
    }
}
