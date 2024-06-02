package io.woorinpang.userservice.core.api.config;

import io.woorinpang.userservice.core.api.config.dto.SocialUser;
import org.springframework.stereotype.Component;

@Component
public class KakaoLogin implements SocialLogin{
    @Override
    public SocialUser verify(String token) {
        SocialUser.SocialUserBuilder socialUserBuilder = SocialUser.builder();
        return socialUserBuilder.build();
    }
}
