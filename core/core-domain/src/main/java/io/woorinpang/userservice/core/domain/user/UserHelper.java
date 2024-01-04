package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserEntity;
import io.woorinpang.userservice.core.db.user.UserEntityRepository;

public final class UserHelper {
    public static UserEntity findUserById(UserEntityRepository userEntityRepository, long userId) {
        return userEntityRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없음"));
    }

    public static UserEntity saveUser(UserEntityRepository userEntityRepository, UserEntity userEntity) {
        return userEntityRepository.save(userEntity);
    }
}
