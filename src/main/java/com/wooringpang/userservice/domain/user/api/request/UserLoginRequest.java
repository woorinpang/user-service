package com.wooringpang.userservice.domain.user.api.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginRequest {

    private String email;
    private String password;
    private String provider;
    private String token;
    private String name;

    /**
     * 사용자 로그인 요청 request 클래스 생성자
     */
    @Builder
    public UserLoginRequest(String email, String password, String provider, String token, String name) {
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.token = token;
        this.name = name;
    }

    /**
     * OAuth 로그인 정보 세팅
     */
    public void setOAuthLoginInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
