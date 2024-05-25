package io.woorinpang.userservice.core.domain.user.domain;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserInfo {
    private String name;

    public ModifyUserCommand toModifyUserCommand() {
        return ModifyUserCommand.builder()
                .name(this.name)
                .build();
    }
}
