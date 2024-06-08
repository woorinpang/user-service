package io.woorinpang.userservice.core.api.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.woorinpang.userservice.core.api.config.dto.SocialUser;
import io.woorinpang.userservice.core.api.support.error.ApiErrorType;
import io.woorinpang.userservice.core.api.support.error.CoreApiException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
public class KakaoLogin implements SocialLogin{
    private final SocialLoginProperties properties;

    public KakaoLogin(SocialLoginProperties socialLoginProperties) {
        this.properties = socialLoginProperties;
    }

    @Override
    public SocialUser verify(String token) {
        SocialUser.SocialUserBuilder socialUserBuilder = SocialUser.builder();

        KakaoIdToken payload = WebClient.create(properties.getKakaoUserInfoUri())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new CoreApiException(ApiErrorType.INVALID_PARAMETER)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new CoreApiException(ApiErrorType.DEFAULT_ERROR)))
                .bodyToMono(KakaoIdToken.class)
                .block();

        if (payload == null) {
            throw new CoreApiException(ApiErrorType.KAKAO_TOKEN_INVALID);
        } else {
            socialUserBuilder
                    .id(String.valueOf(payload.getId()))
                    .name(payload.getAccount().getProfile().getNickname())
                    .email(payload.getAccount().getEmail())
                    .profile(payload.getAccount().getProfile().getProfileImage());
        }

        return socialUserBuilder.build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class KakaoIdToken {
        private long id;

        @JsonProperty(value = "connected_at")
        private LocalDateTime connectedAt;

        @JsonProperty(value = "kakao_account")
        private KakaoAccount account;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        private static class KakaoAccount {
            private String email;

            private KakaoProfile profile;

            @Getter
            @NoArgsConstructor(access = AccessLevel.PROTECTED)
            private static class KakaoProfile {
                private String nickname;

                @JsonProperty("profile_image_url")
                private String profileImage;
            }
        }
    }
}
