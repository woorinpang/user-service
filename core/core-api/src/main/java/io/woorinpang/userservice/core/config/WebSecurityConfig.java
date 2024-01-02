package io.woorinpang.userservice.core.config;

import com.woorinpang.userservice.domain.auth.application.AuthService;
import com.woorinpang.userservice.global.common.constant.GlobalConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(HttpSecurity.class));

        http
                    .csrf().disable()
                    .headers().frameOptions().disable()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //토큰 사용하기 때문에 세션은 비활성화
                .and()
                    .authorizeHttpRequests()
                    .requestMatchers(GlobalConstant.SECURITY_PERMITAIL_ANTPATTERNS).permitAll()
//                    .anyRequest().access(authorizationManager())
                .and()
                    .addFilter(getAuthenticationFilter(authenticationManager))
                    .logout()
                    .logoutSuccessUrl("/");

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
    protected AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, tokenProvider, authService);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration, AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(authService).passwordEncoder(passwordEncoder);
//        return auth.build();
//    }

//    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
//        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, tokenProvider, authService);
//        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
//        authenticationFilter.setAuthenticationManager(authenticationManager(builder));
//        return authenticationFilter;
//    }
}