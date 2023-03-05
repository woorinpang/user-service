package com.woorinpang.userservice.core.user.presentation.request;

import com.woorinpang.userservice.core.user.domain.UserFindPassword;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFindPasswordSaveRequest {

    private String userName;

    private String email;

    private String mainUrl;

    private String changePasswordUrl;

    /**
     * 사용자 비밀번호 찾기 등록 요청 속성 값으로 개인정보처리방침 엔티티 빌더를 사용하여 객체 생성
     */
    public UserFindPassword toEntity(Integer requestNo, String tokenValue) {
        return UserFindPassword.createUserFindPassword()
                .email(this.email)
                .requestNo(requestNo)
                .tokenValue(tokenValue)
                .build();
    }


}
