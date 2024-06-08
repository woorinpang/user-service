package io.woorinpang.userservice.core.api.config.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SocialUser {
    private String id;

    private String email;

    private String name;

    private String profile;

    @Builder
    public SocialUser(String id, String email, String name, String profile) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profile = profile;
    }
}
