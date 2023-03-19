package com.woorinpang.userservice.domain.user.infrastructure.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserListDto {

    private Long userId;
    private String username;
    private String email;
    private String name;
    private String roleCode;
    private String roleDescription;
    private String userStateCode;
    private String userStateDescription;
    private LocalDateTime lastLoginDate;
    private Integer loginFailCount;

    public UserListDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    @QueryProjection
    public UserListDto(Long userId, String username, String email, String name, Role role, UserState userState, LocalDateTime lastLoginDate, Integer loginFailCount) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.name = name;
        this.roleCode = role.getCode();
        this.roleDescription = role.getDescription();
        this.userStateCode = userState.getCode();
        this.userStateDescription = userState.getDescription();
        this.lastLoginDate = lastLoginDate;
        this.loginFailCount = loginFailCount;
    }
}
