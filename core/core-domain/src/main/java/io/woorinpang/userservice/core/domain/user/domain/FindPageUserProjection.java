package io.woorinpang.userservice.core.domain.user.domain;

import com.querydsl.core.annotations.QueryProjection;
import io.woorinpang.userservice.core.enums.user.UserRole;
import io.woorinpang.userservice.core.enums.user.UserState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPageUserProjection {
    private long userId;
    private String email;
    private String name;
    private String userRole;
    private String userRoleDescription;
    private String userState;
    private String userStateDescription;

    @QueryProjection
    public FindPageUserProjection(long userId, String email, String name, UserRole role, UserState state) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.userRole = role.getCode();
        this.userRoleDescription = role.getDescription();
        this.userState = state.getCode();
        this.userStateDescription = state.getDescription();
    }
}
