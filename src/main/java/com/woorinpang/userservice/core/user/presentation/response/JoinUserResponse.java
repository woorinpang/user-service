package com.woorinpang.userservice.core.user.presentation.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JoinUserResponse {

    private Long joinId;
}
