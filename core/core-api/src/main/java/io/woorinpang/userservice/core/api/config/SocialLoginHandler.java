package io.woorinpang.userservice.core.api.config;

import io.woorinpang.userservice.core.api.config.dto.LoginRequest;
import io.woorinpang.userservice.core.api.config.dto.SocialUser;
import io.woorinpang.userservice.core.api.support.error.CoreApiException;
import io.woorinpang.userservice.core.api.support.error.ApiErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class SocialLoginHandler {
    private final Map<String, Function<String, SocialUser>> loginProviders = new HashMap<>();

    public SocialLoginHandler(GoogleLogin googleLogin, KakaoLogin kakaoLogin, NaverLogin naverLogin) {
        loginProviders.put("GOOGLE", googleLogin::verify);
        loginProviders.put("KAKAO", kakaoLogin::verify);
        loginProviders.put("NAVER", naverLogin::verify);
    }

    public SocialUser verifySocialUser(LoginRequest credential) {
        Function<String, SocialUser> loginMethod = loginProviders.get(credential.getProvider());
        log.info("{} login provider", credential.getProvider());

        if(loginMethod == null) {
            throw new CoreApiException(ApiErrorType.PROVIDER_MISMATCH);
        }
        return loginMethod.apply(credential.getToken());
    }
}
