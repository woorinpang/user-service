package io.woorinpang.userservice.core.domain.user.repository;

import io.woorinpang.userservice.core.domain.user.domain.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {
}
