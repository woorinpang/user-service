package com.wooringpang.userservice.core.role.repository;

import com.wooringpang.userservice.core.role.domain.RoleAuthorization;
import com.wooringpang.userservice.core.role.domain.RoleAuthorizationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAuthorizationRepository extends JpaRepository<RoleAuthorization, RoleAuthorizationId> {
}
