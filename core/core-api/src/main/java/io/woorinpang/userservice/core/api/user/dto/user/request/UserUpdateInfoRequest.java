package io.woorinpang.userservice.core.api.user.dto.user.request;

import io.woorinpang.userservice.core.domain.user.dto.command.UserUpdateInfoCommand;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserUpdateInfoRequest {

    private String name;
    private String email;

    public void validate() {
        //validate
    }

    public UserUpdateInfoCommand toCommand() {
        return UserUpdateInfoCommand.builder()
                .name(this.name)
                .email(this.email)
                .build();
    }
}
