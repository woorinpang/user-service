package io.woorinpang.userservice.core.api.controller.user.request;

import io.woorinpang.userservice.core.domain.user.domain.UserInfo;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ModifyUserInfoRequest {
    private String name;

    public void validate() {
        //validate
    }

    public UserInfo toUserInfo() {
        return UserInfo.builder()
                .name(this.name)
                .build();
    }
}
