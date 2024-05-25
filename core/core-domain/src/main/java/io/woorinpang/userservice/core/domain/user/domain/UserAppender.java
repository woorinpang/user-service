package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.woorinpang.userservice.core.domain.user.domain.UserHelper.saveUser;

@Component
@RequiredArgsConstructor
public class UserAppender {
    private final UserRepository userRepository;

    @Transactional
    public long append(UserLogin login, UserInfo info) {
        UserJoinCommand command = UserJoinCommand.builder()
                .email(login.email())
                .password(login.password())
                .name(info.getName())
                .build();

        return saveUser(userRepository, new User(command)).getId();
    }
}
