package io.woorinpang.userservice.admin.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
}
