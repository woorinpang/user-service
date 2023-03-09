package com.woorinpang.userservice.global.config;

import com.woorinpang.userservice.core.user.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                    .csrf().disable()
                    .headers().frameOptions().disable()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //토큰 사용하기 때문에 세션은 비활성화
                .and()
                    .authorizeHttpRequests()
                    .requestMatchers(GlobalConstant.SECURITY_PERMITAIL_ANTPATTERNS).permitAll()
                    .anyRequest().permitAll()
//                    .anyRequest().access("@authorizationService.isAuthorization(request, authentication)") //호출 시 권한 인가 데이터 확인
                .and()
//                .addFilter()
                .logout()
                .logoutSuccessUrl("/");
        return http.build();
    }

//    @Bean
//    protected AuthenticationFilter getAuthenticationFilter() throws Exception {
//        return new AuthenticationFilter(authenticationManager, tokenProvider, userService);
//    }

    @Bean
    public DaoAuthenticationProvider authenticationManager() throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }
}
