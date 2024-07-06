package io.woorinpang.userservice.storage.db.core.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndProviderIsNot(String email, Provider provider);

    Optional<User> findByEmailAndProvider(String email, Provider provider);
}
