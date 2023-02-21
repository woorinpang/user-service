package com.wooringpang.userservice.domain.user.repository;

import com.wooringpang.userservice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySignId(String signId);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndSignIdNot(String email, String signId);
}
