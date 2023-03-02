package com.woorinpang.userservice.global.config;

import com.woorinpang.userservice.core.user.service.UserService;
import com.woorinpang.common.config.GlobalConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //토큰 사용하기 때문에 세션은 비활성화
                .and()
                .authorizeHttpRequests()
                .requestMatchers(GlobalConstant.SECURITY_PERMITAIL_ANTPATTERNS).permitAll()
                .anyRequest().permitAll()
                .and()
                .build();
//                .anyRequest().access("@authorizationService.isAuthorization(request, authentication)") //호출 시 권한 인가 데이터 확인
//                .and()
//                .addFilter(getAuthenticationFilter())
//                .addFilter()
//                .and()
//                .logout()
//                .logoutSuccessUrl("/")
//                .build();
    }

//    @Bean
//    protected AuthenticationFilter getAuthenticationFilter() throws Exception {
//        return new AuthenticationFilter(authenticationManager, tokenProvider, userService);
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
