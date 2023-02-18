package com.wooringpang.userservice.domain.user.repository;

import com.wooringpang.userservice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
