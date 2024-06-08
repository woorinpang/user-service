package io.woorinpang.userservice.core.domain.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FindUser {
    private long id;
    private String email;
    private Provider provider;
    private String password;
    private String name;
    private UserRole userRole;
    private UserState userState;

    public FindUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.provider = user.getProvider();
        this.password = user.getPassword();
        this.name = user.getName();
        this.userRole = user.getRole();
        this.userState = user.getState();
    }
}
