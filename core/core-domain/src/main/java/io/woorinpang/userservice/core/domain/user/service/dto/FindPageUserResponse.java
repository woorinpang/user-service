package io.woorinpang.userservice.core.domain.user.service.dto;

import io.woorinpang.userservice.core.db.user.dto.FindPageUserProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPageUserResponse {
    private long userId;
    private String username;
    private String email;
    private String name;
    private String userRole;
    private String userRoleDescription;
    private String userState;
    private String userStateDescription;

    public FindPageUserResponse(FindPageUserProjection projection) {
        this.userId = projection.getUserId();
        this.username = projection.getUsername();
        this.email = projection.getEmail();
        this.name = projection.getName();
        this.userRole = projection.getUserRole();
        this.userRoleDescription = projection.getUserRoleDescription();
        this.userState = projection.getUserState();
        this.userStateDescription = projection.getUserStateDescription();
    }
}
