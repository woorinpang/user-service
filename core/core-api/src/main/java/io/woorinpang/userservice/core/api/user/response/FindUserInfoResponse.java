package io.woorinpang.userservice.core.api.user.response;

import io.woorinpang.userservice.core.domain.user.FindUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindUserInfoResponse {
    private Long userId;
    private String username;
    private String email;
    private String name;
    private String userRole;
    private String userState;

    public FindUserInfoResponse(FindUser user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.userRole = user.getUserRole().name();
        this.userState = user.getUserState().name();
    }
}
