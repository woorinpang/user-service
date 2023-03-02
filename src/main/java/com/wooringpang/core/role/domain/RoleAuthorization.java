package com.wooringpang.core.role.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleAuthorization {

    @EmbeddedId
    private RoleAuthorizationId roleAuthorizationId;

    @MapsId("authorizationId") //RoleAuthorizationId.authorizationId 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorization_id")
    private Authorization authorization;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Builder(builderMethodName = "createRoleAuthorization")
    public RoleAuthorization(String roleCode, Long authorizationId) {
        this.roleAuthorizationId = RoleAuthorizationId.builder()
                .roleCode(roleCode)
                .authorizationId(authorizationId)
                .build();
        this.authorization = Authorization.createAuthorization()
                .id(authorizationId)
                .build();
    }
}
