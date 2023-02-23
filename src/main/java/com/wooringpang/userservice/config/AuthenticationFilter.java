package com.wooringpang.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooringpang.userservice.core.user.presentation.request.UserLoginRequest;
import com.wooringpang.userservice.core.user.domain.User;
import com.wooringpang.userservice.core.user.service.UserService;
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
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasLength;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService) {
        super.setAuthenticationManager(authenticationManager);
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    /**
     * 로그인 요청시 호출되는 메소드이다.
     * 계정 정보를 받아 인증정보를 생성한다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //사용자가 입력한 인증정보 받기, POST method 값이기 때문에 input stream 으로 받는다
            UserLoginRequest userLoginRequest = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);

            UsernamePasswordAuthenticationToken authenticationToken = null;

            if (userLoginRequest.getProvider() != null && !"email".equals(userLoginRequest.getProvider())) {
                User findUser = userService.loadUserBySocial(userLoginRequest);

                authenticationToken = new UsernamePasswordAuthenticationToken(
                        findUser.getEmail(),
                        null,
                        AuthorityUtils.createAuthorityList(findUser.getRole().getCode())
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return authenticationToken;
            } else {
                authenticationToken = new UsernamePasswordAuthenticationToken(
                        userLoginRequest.getEmail(),
                        userLoginRequest.getPassword(),
                        new ArrayList<>()
                );

                //인증정보 만들기
                return this.getAuthenticationManager().authenticate(authenticationToken);
            }

        } catch (Exception e) {
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
        //토큰 생성 및 response header add
        tokenProvider.createTokenAndAddHeader(request, response, chain, authResult);

        //로그인 성공 후처리
        userService.loginCallback(authResult.getName(), true, "");
    }
    /**
     * 로그인 인증 실패 후 호출된다.
     */
    @Transactional
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String failContent = failed.getMessage();
        if (failed instanceof InternalAuthenticationServiceException) {
            log.info("{} 해당 사용자가 없습니다.", request.getAttribute("email"));
        } else if (failed instanceof BadCredentialsException) {
            failContent = "패스워드 인증에 실패하였습니다. " + failContent;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        //로그인 실패 후처리
        String email = (String) request.getAttribute("email");
        userService.loginCallback(email, false, failContent);
        super.unsuccessfulAuthentication(request, response, failed);
    }

    /**
     * 로그인 요청 뿐만 아니라 모든 요청시마다 호출된다.
     * 토큰에 담긴 정보로 Authentication 정보를 설정한다.
     * 이 처리를 하지 않으면 AnonymousAuthenticationToken 으로 처리된다.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (!hasLength(token) || "undefined".equals(token)) {
                super.doFilter(request, response, chain);
            } else {
                //토큰 유효성 검사는 API Gateway ReactiveAuthorization 클래스에서 미리 처리된다.
                Claims claims = tokenProvider.getClaimsFromToken(token);

                String username = claims.getSubject();
                if (username == null) {
                    //refresh token 에는 subject, authorities 정보가 없다.
                    SecurityContextHolder.getContext().setAuthentication(null);
                } else {
                    List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(tokenProvider.TOKEN_CLAIM_NAME, String.class).split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));
                }

                chain.doFilter(request, response);
            }
        } catch (ServletException | IOException e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            log.error("AuthenticationFilter doFilter error = {}", e.getMessage());
        }
    }
}
