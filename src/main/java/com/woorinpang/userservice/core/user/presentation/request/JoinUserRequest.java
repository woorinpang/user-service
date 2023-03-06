package com.woorinpang.userservice.core.user.presentation.request;

import com.woorinpang.userservice.core.user.application.param.JoinUserParam;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public JoinUserParam toParam() {
        return JoinUserParam.builder()
                .name(this.userName)
                .email(this.email)
                .password(this.password)
                .provider(this.provider)
                .token(this.token)
                .isProvider(isProvider())
                .build();
    }
}
