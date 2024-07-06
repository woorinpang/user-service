package io.woorinpang.userservice.admin.controller.response;

import io.woorinpang.userservice.storage.db.core.user.AdminUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminFindUsersResponse {
    private long userId;
    private String email;
    private String provider;
    private String name;
    private String role;
    private String state;

    public AdminFindUsersResponse(AdminUser user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.provider = user.getProvider().getCode();
        this.name = user.getName();
        this.role = user.getRole().getCode();
        this.state = user.getState().getCode();
    }
}
