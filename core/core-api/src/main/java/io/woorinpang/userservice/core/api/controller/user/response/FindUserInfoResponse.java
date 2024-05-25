package io.woorinpang.userservice.core.api.controller.user.response;

import io.woorinpang.userservice.core.domain.user.domain.FindUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindUserInfoResponse {
    private Long userId;
    private String email;
    private String name;
    private String userRole;
    private String userState;

    public FindUserInfoResponse(FindUser user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.userRole = user.getUserRole().name();
        this.userState = user.getUserState().name();
    }
}
