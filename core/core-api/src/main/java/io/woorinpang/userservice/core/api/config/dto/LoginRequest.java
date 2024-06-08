package io.woorinpang.userservice.core.api.config.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    private String email;
    private String password;
    private String provider;
    private String token;
    private String name;

    public LoginRequest(String email, String password, String provider, String token, String name) {
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.token = token;
        this.name = name;
    }
}
