package io.woorinpang.userservice.core.api.controller.auth.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExistsUsernameRequest {
    private String username;
}
