package com.wooringpang.userservice.domain.log.repository;

import com.wooringpang.userservice.domain.log.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
