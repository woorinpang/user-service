package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserEntity;
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

    public FindUser(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.username = userEntity.getUsername();
        this.email = userEntity.getEmail();
        this.name = userEntity.getName();
        this.userRole = userEntity.getRole().name();
        this.userState = userEntity.getState().name();
    }
}