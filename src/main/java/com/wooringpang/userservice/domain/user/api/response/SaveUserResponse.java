package com.wooringpang.userservice.domain.user.api.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveUserResponse {

    private Long saveId;

    public SaveUserResponse(Long saveId) {
        this.saveId = saveId;
    }
}
