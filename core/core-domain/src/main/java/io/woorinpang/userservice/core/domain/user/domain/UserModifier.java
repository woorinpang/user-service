package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.storage.db.core.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.woorinpang.userservice.core.domain.user.domain.UserRepositoryHelper.*;

@Component
@RequiredArgsConstructor
public class UserModifier {
    private final UserRepository userRepository;

    @Transactional
    public void modify(UserTarget target, String name) {
        findUserById(userRepository, target.id()).modify(name);
    }

    @Transactional
    public void successLogin(UserTarget target) {
        findUserById(userRepository, target.id()).successLogin();
    }

    @Transactional
    public void failLogin(UserTarget target) {
        findUserById(userRepository, target.id()).failLogin();
    }
}
