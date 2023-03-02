package com.woorinpang.userservice.core.role.repository;

import com.woorinpang.userservice.core.role.domain.RoleAuthorizationId;
import com.woorinpang.userservice.core.role.domain.RoleAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAuthorizationRepository extends JpaRepository<RoleAuthorization, RoleAuthorizationId> {
}
