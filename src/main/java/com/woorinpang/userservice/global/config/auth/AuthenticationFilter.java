package com.woorinpang.userservice.global.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.global.config.auth.dto.LoginRequest;
import com.woorinpang.userservice.global.config.jwt.JwtFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtFactory jwtFactory;
    private final UserRepository userRepository;

    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtFactory jwtFactory, UserRepository userRepository) {
        super(authenticationManager);
        this.jwtFactory = jwtFactory;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 사용자가 입력한 인증정보 받기, POST method 값이기 때문에 input stream으로 받았다.
            LoginRequest credential = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    credential.getUsername(),
                    credential.getPassword(),
                    new ArrayList<>()
            );
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = authResult.getName();

        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));

        String accessToken = jwtFactory.accessToken(findUser);
        String refreshToken = jwtFactory.refreshToken();

        //TODO token update
        findUser.updateRefreshToken(refreshToken);

        response.addHeader("access-token", accessToken);
        response.addHeader("refresh-totke", refreshToken);
        response.addHeader("token-id", String.valueOf(findUser.getId()));

        //TODO 로그인 성공 후처리
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String failContent = failed.getMessage();
        if (failed instanceof InternalAuthenticationServiceException) {
            log.info("{} 해당 사용자가 없습니다", request.getAttribute("username"));
        } else if (failed instanceof BadCredentialsException) {
            failContent = "패스워드 인증에 실패하였습니다. " + failContent;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        //TODO 로그인 실패 후처리
        String username = (String) request.getAttribute("username");
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
