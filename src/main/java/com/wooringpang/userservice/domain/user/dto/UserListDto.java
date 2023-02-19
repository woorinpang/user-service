package com.wooringpang.userservice.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wooringpang.userservice.domain.user.entity.Role;
import com.wooringpang.userservice.domain.user.entity.UserStateCode;
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
    private String roleType;
    private String roleName;
    private String userStateCode;
    private String userStateCodeName;
    private LocalDateTime lastLoginDate;
    private Integer loginFailCount;

    @QueryProjection
    public UserListDto(String userId, String username, String email, Role role, UserStateCode userStateCode, LocalDateTime lastLoginDate, Integer loginFailCount) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roleType = role.getCode();
        this.roleName = role.name();
        this.userStateCode = userStateCode.getCode();
        this.userStateCodeName = userStateCode.name();
        this.lastLoginDate = lastLoginDate;
        this.loginFailCount = loginFailCount;
    }
}
