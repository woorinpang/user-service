package io.woorinpang.userservice.core.db.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.woorinpang.userservice.core.db.user.UserRole;
import io.woorinpang.userservice.core.db.user.UserState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPageUserProjection {
    private long userId;
    private String username;
    private String email;
    private String name;
    private String userRole;
    private String userRoleDescription;
    private String userState;
    private String userStateDescription;

    @QueryProjection
    public FindPageUserProjection(long userId, String username, String email, String name, UserRole role, UserState state) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.name = name;
        this.userRole = role.name();
        this.userRoleDescription = role.getDescription();
        this.userState = state.name();
        this.userStateDescription = state.getDescription();
    }
}
