package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserEntity;
import io.woorinpang.userservice.core.db.user.UserRepository;

public final class UserHelper {
    public static UserEntity findUserById(UserRepository userRepository, long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없음"));
    }

    public static UserEntity findUserByUsername(UserRepository userRepository, String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("없음"));
    }

    public static UserEntity saveUser(UserRepository userRepository, UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
