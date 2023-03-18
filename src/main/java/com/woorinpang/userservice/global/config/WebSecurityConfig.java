package com.woorinpang.userservice.global.config;

import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.global.config.auth.AuthenticationFilter;
import com.woorinpang.userservice.global.config.auth.PrincipalDetailService;
import com.woorinpang.userservice.global.config.jwt.JwtFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Profile(("!test"))
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CorsConfig corsConfig;
    private final UserRepository userRepository;
    private final JwtFactory jwtFactory;
    private final PrincipalDetailService principalDetailService;
    private final ObjectPostProcessor<Object> objectPostProcessor;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

        http
                    .addFilter(corsConfig.getCorsFilter())
                    .csrf().disable()
                    .headers().frameOptions().disable()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //토큰 사용하기 때문에 세션은 비활성화
                .and()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .addFilterBefore(encodingFilter, CsrfFilter.class)
                .addFilterBefore(getAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                .and()
                    .authorizeHttpRequests()
                .requestMatchers("/**").permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/");
        return http.build();
    }

    //등록된 AuthenticationManager을 불러오기 위한 Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationManagerBuilder auth = new AuthenticationManagerBuilder(objectPostProcessor);
        auth.userDetailsService(principalDetailService).passwordEncoder(passwordEncoder());
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Filter 등록을 위한 Bean
    protected AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, jwtFactory, userRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /*@Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("1234")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }*/
}
