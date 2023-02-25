package com.wooringpang.userservice.core.role.presentation.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveAuthorizationResponse {

    private Long authorizationId;
}
