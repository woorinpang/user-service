package com.wooringpang.userservice.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wooringpang.userservice.domain.user.entity.Role;
import com.wooringpang.userservice.domain.user.entity.UserState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static org.springframework.util.StringUtils.hasText;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserListDto {

    private String userId;
    private String username;
    private String email;
    private String roleId;
    private String roleName;
    private String userStateId;
    private String userStateCode;
    private LocalDateTime lastLoginDate;
    private Integer loginFailCount;

    @QueryProjection
    public UserListDto(String userId, String username, String email, Role role, UserState userState, LocalDateTime lastLoginDate, Integer loginFailCount) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roleId = role.name();
        this.roleName = role.name();
        this.userStateId = userState.name();
        this.userStateCode = userState.getCode();
        this.lastLoginDate = lastLoginDate;
        this.loginFailCount = loginFailCount;
    }
}
