package io.woorinpang.userservice.core.api.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.woorinpang.userservice.core.api.config.dto.SocialUser;
import io.woorinpang.userservice.core.api.support.error.ApiErrorType;
import io.woorinpang.userservice.core.api.support.error.CoreApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Slf4j
@Component
public class GoogleLogin implements SocialLogin {
    private final SocialLoginProperties properties;

    public GoogleLogin(SocialLoginProperties socialLoginProperties) {
        this.properties = socialLoginProperties;
    }

    @Override
    public SocialUser verify(String token) {
        SocialUser.SocialUserBuilder socialUserBuilder = SocialUser.builder();
        try {
            HttpTransport transport = new NetHttpTransport();
            GsonFactory gsonFactory = new GsonFactory();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
                    .setAudience(Collections.singletonList(properties.getGoogleClientId()))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null) {
                throw new CoreApiException(ApiErrorType.GOOGLE_TOKEN_INVALID);
            } else {
                GoogleIdToken.Payload payload = idToken.getPayload();
                log.info("google oauth2: {}", payload.toString());

                socialUserBuilder
                        .id(payload.getSubject())
                        .email(payload.getEmail())
                        .name((String) payload.get("name"))
                        .profile((String) payload.get("picture"));
            }

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        return socialUserBuilder.build();
    }
}
