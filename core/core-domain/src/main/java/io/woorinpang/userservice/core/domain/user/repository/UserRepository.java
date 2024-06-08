package io.woorinpang.userservice.core.domain.user.repository;

import io.woorinpang.userservice.core.domain.user.domain.User;
import io.woorinpang.userservice.core.domain.user.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndProviderIsNot(String email, Provider provider);
}
