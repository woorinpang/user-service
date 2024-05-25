package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.user.repository.UserRepository;

public final class UserHelper {
    public static User findUserById(UserRepository userRepository, long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없음"));
    }

    public static User findUserByEmail(UserRepository userRepository, String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("없음"));
    }

    public static User saveUser(UserRepository userRepository, User userEntity) {
        return userRepository.save(userEntity);
    }
}
