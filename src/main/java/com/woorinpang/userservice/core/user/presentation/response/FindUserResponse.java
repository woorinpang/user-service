package com.woorinpang.userservice.core.user.presentation.response;

import com.woorinpang.userservice.core.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.util.StringUtils.hasText;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindUserResponse {

    private Long userId;
    private String username;
    private String email;
    private String name;
    private String roleCode;
    private String userStateCode;
    private String googleId;
    private String kakaoId;
    private String naverId;
    private Boolean isSocialUser;
    private Boolean hasPassword;

    public FindUserResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.roleCode = user.getRole().getCode();
        this.userStateCode = user.getUserState().getCode();
        this.googleId = user.getGoogleId();
        this.kakaoId = user.getKakaoId();
        this.naverId = user.getNaverId();
        this.isSocialUser = null;
        this.hasPassword = hasText(user.getPassword());
    }
}
