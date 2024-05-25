package io.woorinpang.userservice.core.api.controller.user.response;

import io.woorinpang.userservice.core.domain.user.domain.FindPageUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPageUserResponse {
    private long userId;
    private String email;
    private String name;
    private String userRole;
    private String userRoleDescription;
    private String userState;
    private String userStateDescription;

    public FindPageUserResponse(FindPageUser projection) {
        this.userId = projection.getUserId();
        this.email = projection.getEmail();
        this.name = projection.getName();
        this.userRole = projection.getUserRole();
        this.userRoleDescription = projection.getUserRoleDescription();
        this.userState = projection.getUserState();
        this.userStateDescription = projection.getUserStateDescription();
    }
}