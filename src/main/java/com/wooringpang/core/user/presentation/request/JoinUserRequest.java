package com.wooringpang.core.user.presentation.request;

import com.wooringpang.core.user.service.param.JoinUserParam;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinUserRequest {

    private String userName;

    private String email;

    private String password;

    private String provider;

    private String token;

    public Boolean isProvider() {
        return Objects.nonNull(this.provider) && !"".equals(provider) && !"undefined".equals(provider)
                && Objects.nonNull(token) && !"".equals(token) && !"undefined".equals(token);
    }

    public JoinUserParam toParam(BCryptPasswordEncoder passwordEncoder) {
        return JoinUserParam.builder()
                .name(this.userName)
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .provider(this.provider)
                .token(this.token)
                .isProvider(isProvider())
                .build();
    }
}
