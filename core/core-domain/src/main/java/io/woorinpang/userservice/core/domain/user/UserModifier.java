package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.woorinpang.userservice.core.domain.user.UserHelper.*;

@Component
@RequiredArgsConstructor
public class UserModifier {
    private final UserRepository userRepository;

    @Transactional
    public void modify(UserTarget target, UserInfo info) {
        findUserById(userRepository, target.id())
                .modify(info.toModifyUserCommand());
    }

    @Transactional
    public void successLogin(UserTarget target) {
        findUserById(userRepository, target.id())
                .successLogin();

    }

    @Transactional
    public void failLogin(UserTarget target) {
        findUserById(userRepository, target.id())
                .failLogin();
    }

    @Transactional
    public void modifyRefreshToken(UserTarget target, String refreshToken) {
        findUserById(userRepository, target.id())
                .modifyRefreshToken(refreshToken);
    }
}
