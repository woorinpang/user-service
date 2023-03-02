package com.woorinpang.userservice.core.role.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RoleListDto {

    private String roleCode;
    private String roleName;
    private String roleContent;
    private LocalDateTime createdDate;

    @QueryProjection
    @Builder
    public RoleListDto(String roleCode, String roleName, String roleContent, LocalDateTime createdDate) {
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.roleContent = roleContent;
        this.createdDate = createdDate;
    }
}
