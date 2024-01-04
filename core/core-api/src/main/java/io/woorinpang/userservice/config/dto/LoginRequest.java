package io.woorinpang.userservice.config.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    private String username;
    private String password;
    private String provider;
    private String token;
    private String name;

    public LoginRequest(String username, String password, String provider, String token, String name) {
        this.username = username;
        this.password = password;
        this.provider = provider;
        this.token = token;
        this.name = name;
    }
}
