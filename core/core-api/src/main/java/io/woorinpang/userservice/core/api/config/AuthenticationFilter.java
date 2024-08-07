package io.woorinpang.userservice.core.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.woorinpang.userservice.core.api.config.dto.LoginRequest;
import io.woorinpang.userservice.core.api.config.dto.LoginUser;
import io.woorinpang.userservice.core.api.config.dto.SocialUser;
import io.woorinpang.userservice.core.api.support.error.CoreApiException;
import io.woorinpang.userservice.core.api.support.util.LogUtil;
import io.woorinpang.userservice.core.domain.user.application.AuthService;
import io.woorinpang.userservice.core.domain.user.domain.FindUser;
import io.woorinpang.userservice.storage.db.core.user.Provider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.jsonwebtoken.lang.Strings.hasLength;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final SocialLoginHandler socialLoginBridge;

    public AuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, AuthService authService, SocialLoginHandler socialLoginBridge) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
        this.authService = authService;
        this.socialLoginBridge = socialLoginBridge;
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
            Authentication authenticationToken = null;

            if (hasText(credential.getProvider()) && Provider.verify(credential.getProvider())) {
                SocialUser socialUser = socialLoginBridge.verifySocialUser(credential);

                FindUser findUser = authService.loadUserBySocial(socialUser.getEmail(), socialUser.getName(), Provider.findByCode(credential.getProvider()));

                authenticationToken = new UsernamePasswordAuthenticationToken(
                        findUser.getEmail(),
                        null,
                        AuthorityUtils.createAuthorityList(findUser.getUserRole().getCode()));
            } else {
                authenticationToken = new UsernamePasswordAuthenticationToken(
                        credential.getEmail(),
                        credential.getPassword(),
                        new ArrayList<>()
                );

                // 인증정보 만들기
                authenticationToken = getAuthenticationManager().authenticate(authenticationToken);
            }
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return authenticationToken;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = authResult.getName();
        String authorities = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        FindUser findUser = authService.findUser(email);

        LoginUser loginUser = new LoginUser(findUser);
        String payload = new ObjectMapper().writeValueAsString(loginUser);

        String accessToken = tokenProvider.createAccessToken(email, authorities, payload);
        String refreshToken = tokenProvider.createRefreshToken();

        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponse));

        // 로그인 성공 후처리
        authService.loginCallback(LogUtil.getSiteId(request), LogUtil.getUserIp(), authResult.getName(), true, "");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String failContent = failed.getMessage();
        if (failed instanceof InternalAuthenticationServiceException) {
            log.info("{} 해당 사용자가 없습니다", request.getAttribute("email"));
        } else if (failed instanceof BadCredentialsException) {
            failContent = "패스워드 인증에 실패하였습니다. " + failContent;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // 로그인 실패 후처리
        String email = (String) request.getAttribute("email");
        authService.loginCallback(LogUtil.getSiteId(request), LogUtil.getUserIp(), email, false, failContent);
        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (!hasLength(token) || "undefined".equals(token)) {
                super.doFilter(request, response, chain);
            } else {
                // 토큰 유효성 검사는 API Gateway ReactiveAuthorization 클래스에서 미리 처리된다.
                Claims claims = tokenProvider.getClaimsFromToken(token.replace("Bearer ", ""));

                String email = claims.getSubject();
                if (email == null) {
                    // refresh token 에는 subject, authorities 정보가 없다.
                    SecurityContextHolder.getContext().setAuthentication(null);
                } else {
                    List<SimpleGrantedAuthority> roleList = Arrays.stream(claims.get(tokenProvider.TOKEN_CLAIM_NAME, String.class).split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null, roleList));
                }

                chain.doFilter(request, response);
            }
        } catch (CoreApiException e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(e.getType().getStatus().value());
            log.error("AuthenticationFilter doFilter error: {}", e.getMessage(), e);
        } catch (ServletException | IOException e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            log.error("AuthenticationFilter doFilter error: {}", e.getMessage(), e);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class LoginResponse {
        private String accessToken;
        private String refreshToken;

        public LoginResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
