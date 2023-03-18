package com.woorinpang.userservice.domain.user.presentation.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JoinUserResponse {

    private Long joinId;
}
