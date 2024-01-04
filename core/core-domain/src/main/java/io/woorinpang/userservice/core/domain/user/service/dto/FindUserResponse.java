package io.woorinpang.userservice.core.domain.user.service.dto;

import io.woorinpang.userservice.core.db.user.UserEntity;
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

    public FindUserResponse(UserEntity userEntity) {
        this.userId = userEntity.getId();
        this.username = userEntity.getUsername();
        this.email = userEntity.getEmail();
        this.name = userEntity.getName();
        this.userRole = userEntity.getRole().name();
        this.userRoleDescription = userEntity.getRole().getDescription();
        this.userState = userEntity.getState().name();
        this.userStateDescription = userEntity.getState().getDescription();
    }
}
