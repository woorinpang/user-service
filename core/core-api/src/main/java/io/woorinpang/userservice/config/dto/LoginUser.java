package io.woorinpang.userservice.config.dto;

import io.woorinpang.userservice.core.domain.user.FindUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginUser {
    private long userId;
    private String username;
    private String email;
    private String name;
    private String role;

    public LoginUser(FindUser user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getUserRole().getCode();
    }
}
