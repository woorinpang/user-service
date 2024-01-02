package io.woorinpang.userservice.core.api.user.dto.user.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserExistsUsernameRequest {
    private String username;
}
