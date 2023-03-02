package com.woorinpang.userservice.core.log.repository;

import com.woorinpang.userservice.core.log.domain.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
