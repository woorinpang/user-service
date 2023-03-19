package com.woorinpang.userservice.domain.log.infrastructure;

import com.woorinpang.userservice.domain.log.domain.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {
}
