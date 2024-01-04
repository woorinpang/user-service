package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.dto.ModifyUserCommand;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserInfo {
    private String email;
    private String name;


    public ModifyUserCommand toModifyUserCommand() {
        return ModifyUserCommand.builder()
                .email(this.email)
                .name(this.name)
                .build();
    }
}
