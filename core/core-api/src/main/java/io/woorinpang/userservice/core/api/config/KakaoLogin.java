package io.woorinpang.userservice.core.api.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.woorinpang.userservice.core.api.config.dto.SocialUser;
import io.woorinpang.userservice.core.api.support.error.CoreApiException;
import io.woorinpang.userservice.core.api.support.error.ErrorType;
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
    private final SocialLoginProperties socialLoginProperties;

    public KakaoLogin(SocialLoginProperties socialLoginProperties) {
        this.socialLoginProperties = socialLoginProperties;
    }

    @Override
    public SocialUser verify(String token) {
        SocialUser.SocialUserBuilder socialUserBuilder = SocialUser.builder();

        KakaoIdToken payload = WebClient.create("https://kapi.kakao.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoIdToken.class)
                .block();

        if (payload == null) {
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        } else {
            socialUserBuilder.id(String.valueOf(payload.getId()))
                    .name(payload.getKakaoAccount().getProfile().getNickname())
                    .email(payload.getKakaoAccount().getEmail());
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
        private KakaoAccount kakaoAccount;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        private static class KakaoAccount {
            private String email;

            private KakaoProfile profile;

            @Getter
            @NoArgsConstructor(access = AccessLevel.PROTECTED)
            private static class KakaoProfile {
                private String nickname;
            }
        }
    }
}
