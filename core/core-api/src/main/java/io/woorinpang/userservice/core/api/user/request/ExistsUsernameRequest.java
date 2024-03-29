package io.woorinpang.userservice.core.api.user.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExistsUsernameRequest {
    private String username;
}
