package com.woorinpang.userservice.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woorinpang.userservice.domain.auth.application.AuthService;
import com.woorinpang.userservice.global.config.auth.dto.LoginRequest;
import com.woorinpang.userservice.global.exception.BusinessException;
import com.woorinpang.userservice.global.util.LogUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.jsonwebtoken.lang.Strings.hasLength;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;
    private final AuthService authService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, AuthService authService) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
        this.authService = authService;
    }

    /**
     * 로그인 요청 시 호출되는 메소드이다.
     * 계정 정보를 받아 인증정보를 생성한다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 사용자가 입력한 인증정보 받기, POST method 값이기 때문에 input stream으로 받았다.
            LoginRequest credential = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            UsernamePasswordAuthenticationToken authenticationToken = null;
            if (credential.getProvider() != null && !"email".equals(credential.getProvider())) {
                //TODO social login
                return null;
            } else {
                authenticationToken = new UsernamePasswordAuthenticationToken(
                        credential.getUsername(),
                        credential.getPassword(),
                        new ArrayList<>()
                );

                // 인증정보 만들기
                return getAuthenticationManager().authenticate(authenticationToken);
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 로그인 인증 성공 후 호출된다.
     * 토큰을 생성하여 헤더에 토큰 정보를 담는다.
     */
    @Transactional
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 토큰 생성 및 response header add
        tokenProvider.createTokenAndAddHeader(request, response, chain, authResult);
        // 로그인 성공 후처리
        authService.loginCallback(LogUtil.getSiteId(request), authResult.getName(), true, "");
    }

    @Transactional
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String failContent = failed.getMessage();
        if (failed instanceof InternalAuthenticationServiceException) {
            log.info("{} 해당 사용자가 없습니다", request.getAttribute("username"));
        } else if (failed instanceof BadCredentialsException) {
            failContent = "패스워드 인증에 실패하였습니다. " + failContent;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // 로그인 실패 후처리
        String username = (String) request.getAttribute("username");
        authService.loginCallback(LogUtil.getSiteId(request), username, false, failContent);
        super.unsuccessfulAuthentication(request, response, failed);
    }


    /**
     * 로그인 요청 뿐만 아니라 모든 요청시마다 호출된다.
     * 토큰에 담긴 정보로 Authentication 정보를 설정한다.
     * 이 처리를 하지 않으면 AnonymousAuthenticationToken 으로 처리된다.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (!hasLength(token) || "undefined".equals(token)) {
                super.doFilter(request, response, chain);
            } else {
                // 토큰 유효성 검사는 API Gateway ReactiveAuthorization 클래스에서 미리 처리된다.
                Claims claims = tokenProvider.getClaimsFromToken(token);

                String username = claims.getSubject();
                if (username == null) {
                    // refresh token 에는 subject, authorities 정보가 없다.
                    SecurityContextHolder.getContext().setAuthentication(null);
                } else {
                    List<SimpleGrantedAuthority> roleList = Arrays.stream(claims.get(tokenProvider.TOKEN_CLAIM_NAME, String.class).split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, roleList));
                }

                chain.doFilter(request, response);
            }
        } catch (BusinessException e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(e.getErrorCode().getStatus());
            log.error("AuthenticationFilter doFilter error: {}", e.getMessage());
        } catch (ServletException | IOException e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            log.error("AuthenticationFilter doFilter error: {}", e.getMessage());
        }
    }
}
