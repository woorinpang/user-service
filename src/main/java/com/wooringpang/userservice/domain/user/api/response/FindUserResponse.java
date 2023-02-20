package com.wooringpang.userservice.domain.user.api.response;

import com.wooringpang.userservice.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.util.StringUtils.hasText;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindUserResponse {

    private Long userId;
    private String signId;
    private String username;
    private String email;
    private String roleCode;
    private String userStateCode;
    private String googleId;
    private String kakaoId;
    private String naverId;
    private Boolean isSocialUser;
    private Boolean hasPassword;

    public FindUserResponse(User user) {
        this.userId = user.getId();
        this.signId = user.getSignId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roleCode = user.getRole().getCode();
        this.userStateCode = user.getUserState().getCode();
        this.googleId = user.getGoogleId();
        this.kakaoId = user.getKakaoId();
        this.naverId = user.getNaverId();
        this.isSocialUser = null;
        this.hasPassword = hasText(user.getPassword());
    }
}
