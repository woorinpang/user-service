package com.wooringpang.userservice.domain.user.api.response;

import com.wooringpang.userservice.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.util.StringUtils.hasText;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindUserResponse {

    private String userId;
    private String username;
    private String email;
    private String roleId;
    private String userStateCode;
    private String googleId;
    private String kakaoId;
    private String naverId;
    private Boolean isSocialUser;
    private Boolean hasPassword;

    public FindUserResponse(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roleId = user.getRole().name();
        this.userStateCode = user.getUserStateCode();
        this.googleId = user.getGoogleId();
        this.kakaoId = user.getKakaoId();
        this.naverId = user.getNaverId();
        this.isSocialUser = null;
        this.hasPassword = hasText(user.getPassword());
    }
}
