package io.woorinpang.userservice.core.api.controller.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ModifyUserRequest {
    @NotBlank(message = "이름은 필수입니다.")
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
