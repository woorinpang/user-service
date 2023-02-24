package com.wooringpang.userservice.global.config;

import com.wooringpang.userservice.core.user.domain.User;
import com.wooringpang.userservice.core.user.service.UserService;
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
    private final UserService userService;

    public TokenProvider(UserService userService) {
        this.userService = userService;
    }

    @Value("${token.secret}")
    private String TOKEN_SECRET;

    @Value("${token.expiration_time}")
    private String TOKEN_EXPIRATION_TIME;

    @Value("${token.refresh_time")
    private String TOKEN_REFRESH_TIME;

    final String TOKEN_CLAIM_NAME = "authorities";
    final String TOKEN_ACCESS_KEY = "access-token";
    final String TOKEN_REFRESH_KEY = "refresh-token";
    final String TOKEN_USER_ID = "token-id";

    /**
     * 로그인 후 토큰을 생성하고 헤더에 정보를 담는다.
     */
    public void createTokenAndAddHeader(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        //로그인 성공 후 토큰 처리
        String email = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //signId 가져오기
        User findUser = userService.findByEmail(email);
        String signId = findUser.getSignId();

        //jwt access 토큰 생성
        String accessToken = createAccessToken(authorities, signId);

        //jwt refresh 토크 생성 후 사용자 도메인에 저장하여 토큰 재생성 요청시 활용한다.
        String refreshToken = createRefreshToken();
        userService.updateRefreshToken(signId, refreshToken);

        //Header 에 토큰 세팅
        response.addHeader(TOKEN_ACCESS_KEY, accessToken);
        response.addHeader(TOKEN_REFRESH_KEY, refreshToken);
        response.addHeader(TOKEN_USER_ID, signId);
    }

    /**
     * jwt access token 생성
     */
    private String createAccessToken(String authorities, String signId) {
        return Jwts.builder()
                .setSubject(signId)
                .claim(TOKEN_CLAIM_NAME, authorities)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(TOKEN_EXPIRATION_TIME)))
                .signWith(SignatureAlgorithm.ES512, TOKEN_SECRET)
                .compact();
    }

    /**
     * jwt refresh token 생성
     * 중복 로그인을 허용하려면 user domain 에 있는 refresh token 값을 반환하고 없는 경우에만 생성하도록 처리한다.
     */
    private String createRefreshToken() {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(TOKEN_REFRESH_TIME)))
                .signWith(SignatureAlgorithm.ES512, TOKEN_SECRET)
                .compact();
    }

    /**
     * 사용자가 있으면 access toke 을 새로 발급하여 반환한다.
     */
    public String refreshToken(String refreshToken, HttpServletResponse response) {
        //refresh token 으로 유효한 사용자가 있는지 찾는다.
        User findUser = userService.findByRefreshToken(refreshToken);

        //사용자가 있으면 access token 을 새로 발급하여 반환합니다.
        String accessToken = createAccessToken(findUser.getRole().getCode(), findUser.getSignId());

        String filteredRefreshToken = refreshToken.replaceAll("\r", "").replaceAll("\n", "");

        //Header 에 토큰 세팅
        response.addHeader(TOKEN_ACCESS_KEY, accessToken);
        response.addHeader(TOKEN_REFRESH_KEY, filteredRefreshToken);
        response.addHeader(TOKEN_USER_ID, findUser.getSignId());
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
