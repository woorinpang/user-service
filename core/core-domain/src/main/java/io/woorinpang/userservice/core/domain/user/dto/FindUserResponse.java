package io.woorinpang.userservice.core.domain.user.dto;

import io.woorinpang.userservice.core.db.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindUserResponse {
    private long userId;
    private String username;
    private String email;
    private String name;
    private String userRole;
    private String userRoleDescription;
    private String userState;
    private String userStateDescription;

    public FindUserResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.userRole = user.getRole().name();
        this.userRoleDescription = user.getRole().getDescription();
        this.userState = user.getState().name();
        this.userStateDescription = user.getState().getDescription();
    }
}
