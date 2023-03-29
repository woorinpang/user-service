package com.woorinpang.userservice.domain.user.presentation.user.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserJoinResponse {
    private Long joinedUserId;
}
