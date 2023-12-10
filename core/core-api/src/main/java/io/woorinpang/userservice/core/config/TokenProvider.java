package io.woorinpang.userservice.core.config;

import com.woorinpang.userservice.domain.auth.application.AuthService;
import com.woorinpang.userservice.domain.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final AuthService authService;

    public TokenProvider(AuthService authService) {
        this.authService = authService;
    }

    @Value("${token.access_expiration_time}")
    private String TOKEN_EXPIRATION_TIME;

    @Value("${token.refresh_expiration_time}")
    private String TOKEN_REFRESH_TIME;

    @Value("${token.secret-key}")
    private String TOKEN_SECRET;

    final String TOKEN_CLAIM_NAME = "authorities";
    final String TOKEN_ACCESS_KEY = "access-token";
    final String TOKEN_REFRESH_KEY = "refresh-token";
    final String TOKEN_USER_ID = "token-id";

    /**
     * 로그인 후 토큰을 생성하고 헤더에 정보를 담는다.
     */
    public void createTokenAndAddHeader(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        // 로그인 성공 후 토큰 처리
        String username = authResult.getName();
        String authorities = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // userid 가져오기
        User findUser = authService.findUserByUsername(username);
        Long userId = findUser.getId();

        // JWT Access 토큰 생성
        String accessToken = createAccessToken(authorities,  userId, findUser.getUsername());

        // JWT Refresh 토큰 생성 후 사용자 도메인에 저장하여 토큰 재생성 요청시 활용한다.
        String refreshToken = createRefreshToken();
        authService.updateRefreshToken(userId, refreshToken);

        // Header에 토큰 세팅
        response.addHeader(TOKEN_ACCESS_KEY, accessToken);
        response.addHeader(TOKEN_REFRESH_KEY, refreshToken);
        response.addHeader(TOKEN_USER_ID, userId.toString());
    }

    /**
     * JWT Access Token 생성
     */
    private String createAccessToken(String authorities, Long userId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim(TOKEN_CLAIM_NAME, authorities)
                .claim("userId", userId)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(TOKEN_EXPIRATION_TIME)))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();
    }

    /**
     * JWT Refresh Token 생성
     * 중복 로그인을 허용하려면 user domain 에 있는 refresh token 값을 반환하고 없는 경우에만 생성하도록 처리한다.
     */
    private String createRefreshToken() {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(TOKEN_REFRESH_TIME)))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();
    }

    /**
     * 사용자가 있으면 access token 을 새로 발급하여 리턴한다.
     */
    public String refreshToken(String refreshToken, HttpServletResponse response) {
        // refresh token 으로 유효한 사용자가 있는지 찾는다.
        User findUser = authService.findByRefreshToken(refreshToken);
        // 사용자가 있으면 access token 을 새로 발급하여 리턴한다.
        String accessToken = createAccessToken(findUser.getRole().getCode(), findUser.getId(), findUser.getUsername());

        String filteredRefreshToken = refreshToken.replaceAll("\r", "").replaceAll("\n", "");

        // Header에 토큰 세팅
        response.addHeader(TOKEN_ACCESS_KEY, accessToken);
        response.addHeader(TOKEN_REFRESH_KEY, filteredRefreshToken);
        response.addHeader(TOKEN_USER_ID, findUser.getId().toString());
        return accessToken;
    }

    /**
     * AuthenticationFilter.doFilter 메소드에서 UsernamePasswordAuthenticationToken 정보를 세팅할 때 호출된다.
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
