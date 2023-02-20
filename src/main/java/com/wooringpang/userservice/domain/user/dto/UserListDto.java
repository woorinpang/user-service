package com.wooringpang.userservice.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wooringpang.userservice.domain.user.entity.Role;
import com.wooringpang.userservice.domain.user.entity.User;
import com.wooringpang.userservice.domain.user.entity.UserState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static org.springframework.util.StringUtils.hasText;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserListDto {

    private Long userId;
    private String signId;
    private String username;
    private String email;
    private String roleCode;
    private String roleDescription;
    private String userStateCode;
    private String userStateDescription;
    private LocalDateTime lastLoginDate;
    private Integer loginFailCount;

    public UserListDto(User user) {
        this.userId = user.getId();
        this.signId = user.getSignId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    @QueryProjection
    public UserListDto(Long userId, String signId, String username, String email, Role role, UserState userState, LocalDateTime lastLoginDate, Integer loginFailCount) {
        this.userId = userId;
        this.signId = signId;
        this.username = username;
        this.email = email;
        this.roleCode = role.getCode();
        this.roleDescription = role.getDescription();
        this.userStateCode = userState.getCode();
        this.userStateDescription = userState.getDescription();
        this.lastLoginDate = lastLoginDate;
        this.loginFailCount = loginFailCount;
    }
}
