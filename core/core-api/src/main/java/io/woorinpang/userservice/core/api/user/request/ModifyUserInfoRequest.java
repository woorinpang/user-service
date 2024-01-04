package io.woorinpang.userservice.core.api.user.request;

import io.woorinpang.userservice.core.domain.user.UserInfo;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ModifyUserInfoRequest {
    @Email
    private String email;
    private String name;

    public void validate() {
        //validate
    }

    public UserInfo toUserInfo() {
        return UserInfo.builder()
                .email(this.email)
                .name(this.name)
                .build();
    }
}
