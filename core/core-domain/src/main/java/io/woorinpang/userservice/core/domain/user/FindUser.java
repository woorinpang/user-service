package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindUser {
    private long id;
    private String username;
    private String email;
    private String name;
    private String userRole;
    private String userState;

    public FindUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.userRole = user.getRole().name();
        this.userState = user.getState().name();
    }
}
