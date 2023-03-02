package com.wooringpang.core.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wooringpang.core.user.domain.Role;
import com.wooringpang.core.user.domain.User;
import com.wooringpang.core.user.domain.UserState;
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
    private String name;
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
        this.name = user.getName();
        this.email = user.getEmail();
    }

    @QueryProjection
    public UserListDto(Long userId, String signId, String name, String email, Role role, UserState userState, LocalDateTime lastLoginDate, Integer loginFailCount) {
        this.userId = userId;
        this.signId = signId;
        this.name = name;
        this.email = email;
        this.roleCode = role.getCode();
        this.roleDescription = role.getDescription();
        this.userStateCode = userState.getCode();
        this.userStateDescription = userState.getDescription();
        this.lastLoginDate = lastLoginDate;
        this.loginFailCount = loginFailCount;
    }
}
