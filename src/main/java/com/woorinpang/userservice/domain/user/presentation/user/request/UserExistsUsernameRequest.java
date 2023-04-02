package com.woorinpang.userservice.domain.user.presentation.user.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserExistsUsernameRequest {
    private String username;
}
