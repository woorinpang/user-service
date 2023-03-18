package com.woorinpang.userservice.global.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.woorinpang.userservice.domain.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtFactory {
    @Value("${token.secret}")
    private String SECRET;
    @Value("${token.access_expiration_time}")
    private long ACCESS_EXPIRATION_TIME;
    @Value("${token.refresh_expiration_time}")
    private long REFRESH_EXPIRATION_TIME;

    public String accessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("name", user.getName())
                .withClaim("auth", user.getRole().getCode())
                .sign(Algorithm.HMAC512(SECRET));
    }

    public String refreshToken() {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET));
    }
}
