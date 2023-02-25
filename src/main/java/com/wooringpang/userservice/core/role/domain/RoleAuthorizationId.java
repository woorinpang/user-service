package com.wooringpang.userservice.core.role.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class RoleAuthorizationId implements Serializable {

    private static final long serialVersionUID = 4225385308499664433L;

    @Column(length = 30)
    private String roleCode;
    private Long authorizationId; //@MapId("authorizationId")로 매핑

    @Builder
    public RoleAuthorizationId(String roleCode, Long authorizationId) {
        this.roleCode = roleCode;
        this.authorizationId = authorizationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleCode, authorizationId);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof RoleAuthorizationId)) return  false;
        RoleAuthorizationId that = (RoleAuthorizationId) object;
        return Objects.equals(roleCode, that.getRoleCode()) &&
                Objects.equals(authorizationId, that.getAuthorizationId());
    }
}
