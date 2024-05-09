package io.woorinpang.userservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.woorinpang.userservice.core.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private final AuthService authService;

    @Value("${token.access_expiration_time}")
    private String TOKEN_ACCESS_TIME;

    @Value("${token.refresh_expiration_time}")
    private String TOKEN_REFRESH_TIME;

    @Value("${token.secret-key}")
    private String TOKEN_SECRET_KEY;

    public static final String BEARER = "Bearer ";
    public static final String TOKEN_CLAIM_NAME = "authorities";
    public static final String TOKEN_ACCESS_KEY = "access-token";
    public static final String TOKEN_REFRESH_KEY = "refresh-token";

    public String createAccessToken(String username, String authorities, String payload) {
        return Jwts.builder()
                .setSubject(username)
//                .setPayload(payload)
                .claim(TOKEN_CLAIM_NAME, authorities)
                .claim("user", payload)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(TOKEN_ACCESS_TIME)))
                .signWith(this.getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(TOKEN_REFRESH_TIME)))
                .signWith(this.getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(this.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(TOKEN_SECRET_KEY));
    }
}
