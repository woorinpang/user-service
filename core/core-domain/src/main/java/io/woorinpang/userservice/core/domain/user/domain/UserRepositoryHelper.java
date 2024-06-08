package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.support.error.CoreDomainException;
import io.woorinpang.userservice.core.domain.support.error.DomainErrorType;
import io.woorinpang.userservice.core.domain.user.repository.UserRepository;

public final class UserRepositoryHelper {
    public static User findUserById(UserRepository repository, long userId) {
        return repository
                .findById(userId)
                .orElseThrow(() -> new CoreDomainException(DomainErrorType.USER_ENTITY_NOT_FOUND));
    }

    public static User findUserByEmail(UserRepository repository, String email) {
        return repository
                .findByEmail(email)
                .orElseThrow(() -> new CoreDomainException(DomainErrorType.USER_ENTITY_NOT_FOUND));
    }

    public static User saveUser(UserRepository repository, User user) {
        return repository.save(user);
    }

    public static boolean existsByEmail(UserRepository repository, String email) {
        return repository.existsByEmail(email);
    }
}
