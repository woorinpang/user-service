package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.User;
import io.woorinpang.userservice.core.db.user.UserRepository;
import io.woorinpang.userservice.core.db.user.dto.UserJoinCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserAppender {
    private final UserRepository userRepository;

    @Transactional
    public long append(UserLogin login, UserInfo info) {
        UserJoinCommand command = UserJoinCommand.builder()
                .username(login.id())
                .password(login.password())
                .email(info.getEmail())
                .name(info.getName())
                .build();
        return userRepository.save(new User(command)).getId();
    }
}
