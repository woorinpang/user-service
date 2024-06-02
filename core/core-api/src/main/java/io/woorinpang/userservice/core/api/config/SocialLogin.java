package io.woorinpang.userservice.core.api.config;

import io.woorinpang.userservice.core.api.config.dto.SocialUser;

public interface SocialLogin {
    SocialUser verify(String token);
}
