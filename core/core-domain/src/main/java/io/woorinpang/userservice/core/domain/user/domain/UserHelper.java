package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.support.error.CoreDomainException;
import io.woorinpang.userservice.core.domain.support.error.DomainErrorType;
import io.woorinpang.userservice.core.domain.user.repository.UserRepository;

public final class UserHelper {
    public static User findUserById(UserRepository userRepository, long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new CoreDomainException(DomainErrorType.USER_NOT_FOUND));
    }

    public static User findUserByEmail(UserRepository userRepository, String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new CoreDomainException(DomainErrorType.USER_NOT_FOUND));
    }

    public static User saveUser(UserRepository userRepository, User userEntity) {
        return userRepository.save(userEntity);
    }
}
