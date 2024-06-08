package io.woorinpang.userservice.core.api.config.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    private String email;
    private String password;
    private String provider;
    private String token;

    public LoginRequest(String email, String password, String provider, String token) {
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.token = token;
    }
}
