package io.woorinpang.userservice.core.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SocialLoginProperties {
    @Value(value = "${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value(value = "${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientSecret;

    @Value(value = "${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value(value = "${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientSecret;

    @Value(value = "${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value(value = "${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientSecret;

    @Value(value = "${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    @Value(value = "${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String naverUserInfoUri;
}
