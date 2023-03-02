package com.woorinpang.userservice.core.role.repository;

import com.woorinpang.userservice.core.role.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
