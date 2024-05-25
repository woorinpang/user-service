package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.user.repository.UserRepository;

public final class UserHelper {
    public static User findUserById(UserRepository userRepository, long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없음"));
    }

    public static User findUserByUsername(UserRepository userRepository, String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("없음"));
    }

    public static User saveUser(UserRepository userRepository, User userEntity) {
        return userRepository.save(userEntity);
    }
}
