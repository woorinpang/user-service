package com.woorinpang.userservice.global.config;

import com.woorinpang.userservice.domain.auth.application.AuthService;
import com.woorinpang.userservice.global.common.constant.GlobalConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(HttpSecurity.class));
        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, tokenProvider, authService);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(GlobalConstant.SECURITY_PERMITAIL_ANTPATTERNS).permitAll()
//                        .anyRequest().access("@authorizationService.isAuthorization(request, authentication)") // 호출 시 권한 인가 데이터 확인
                )
                .addFilter(authenticationFilter)
                .logout(logout -> logout.logoutSuccessUrl("/"))
        ;

        return http.build();
    }

    /*@Bean
    AuthorizationManager<RequestAuthorizationContext> requestMatcherAuthorizationManager(HandlerMappingIntrospector introspector) {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        RequestMatcher permitAll =
                new AndRequestMatcher(
                        mvcMatcherBuilder.pattern("/resources/**"),
                        mvcMatcherBuilder.pattern("/signup"),
                        mvcMatcherBuilder.pattern("/about"));
        RequestMatcher admin = mvcMatcherBuilder.pattern("/admin/**");
        RequestMatcher db = mvcMatcherBuilder.pattern("/db/**");
        RequestMatcher any = AnyRequestMatcher.INSTANCE;
        AuthorizationManager<HttpServletRequest> manager = RequestMatcherDelegatingAuthorizationManager.builder()
                .add(permitAll, (context) -> new AuthorizationDecision(true))
                .add(admin, AuthorityAuthorizationManager.hasRole("ADMIN"))
                .add(db, AuthorityAuthorizationManager.hasRole("DBA"))
                .add(any, new AuthenticatedAuthorizationManager())
                .build();
        return (context) -> manager.check(context.getRequest());*/

    //등록된 AuthenticationManager을 불러오기 위한 Bean
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    //Filter 등록을 위한 Bean
//    protected AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
//        return new AuthenticationFilter(authenticationManager, tokenProvider, authService);
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
}
