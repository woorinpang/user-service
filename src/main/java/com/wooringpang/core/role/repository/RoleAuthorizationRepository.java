package com.wooringpang.core.role.repository;

import com.wooringpang.core.role.domain.RoleAuthorizationId;
import com.wooringpang.core.role.domain.RoleAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAuthorizationRepository extends JpaRepository<RoleAuthorization, RoleAuthorizationId> {
}
