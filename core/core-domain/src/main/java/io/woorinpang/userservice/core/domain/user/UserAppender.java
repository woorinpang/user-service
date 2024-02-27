package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.storage.core.db.user.UserEntity;
import io.woorinpang.userservice.storage.core.db.user.UserRepository;
import io.woorinpang.userservice.storage.core.db.user.dto.UserJoinCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.woorinpang.userservice.core.domain.user.UserHelper.saveUser;

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

        return saveUser(userRepository, new UserEntity(command)).getId();
    }
}
