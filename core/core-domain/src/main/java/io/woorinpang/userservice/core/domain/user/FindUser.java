package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.enums.user.UserRole;
import io.woorinpang.userservice.core.enums.user.UserState;
import io.woorinpang.userservice.storage.core.db.user.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FindUser {
    private long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private UserRole userRole;
    private UserState userState;

    public FindUser(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.name = user.getName();
        this.userRole = user.getRole();
        this.userState = user.getState();
    }
}
