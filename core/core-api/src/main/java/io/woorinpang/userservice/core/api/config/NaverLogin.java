package io.woorinpang.userservice.core.api.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.woorinpang.userservice.core.api.config.dto.SocialUser;
import io.woorinpang.userservice.core.api.support.error.CoreApiException;
import io.woorinpang.userservice.core.api.support.error.ApiErrorType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class NaverLogin implements SocialLogin {
    private final SocialLoginProperties properties;

    public NaverLogin(SocialLoginProperties socialLoginProperties) {
        this.properties = socialLoginProperties;
    }

    @Override
    public SocialUser verify(String token) {
        SocialUser.SocialUserBuilder socialUserBuilder = SocialUser.builder();

        NaverIdToken payload = WebClient.create(properties.getNaverUserInfoUri())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new CoreApiException(ApiErrorType.INVALID_PARAMETER)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new CoreApiException(ApiErrorType.DEFAULT_ERROR)))
                .bodyToMono(NaverIdToken.class)
                .block();

        if (payload == null) {
            throw new CoreApiException(ApiErrorType.NAVER_TOKEN_INVALID);
        } else {
            socialUserBuilder
                    .id(String.valueOf(payload.getAccount().getId()))
                    .name(payload.getAccount().getName())
                    .email(payload.getAccount().getId())
                    .profile(payload.getAccount().getProfileImage())
            ;
        }

        return socialUserBuilder.build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class NaverIdToken {
        @JsonProperty("resultcode")
        private String code;

        private String message;

        @JsonProperty("response")
        private NaverAccount account;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        private static class NaverAccount {
            private String id;

            private String name;

            private String nickname;

            @JsonProperty("profile_image")
            private String profileImage;

            private String mobile;
        }
    }
}
