package io.woorinpang.userservice.core.api.user.dto.user.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserJoinResponse {
    private Long userId;
}
