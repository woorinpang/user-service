package com.woorinpang.userservice.core.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFindPasswordRepository extends JpaRepository<UserFindPassword, UserFindPasswordId> {
    Optional<UserFindPassword> findByTokenValue(String tokenValue);
}
