package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserEntity;
import io.woorinpang.userservice.core.db.user.UserEntityRepository;
import io.woorinpang.userservice.core.db.user.dto.UserJoinCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.woorinpang.userservice.core.domain.user.UserHelper.saveUser;

@Component
@RequiredArgsConstructor
public class UserAppender {
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public long append(UserLogin login, UserInfo info) {
        UserJoinCommand command = UserJoinCommand.builder()
                .username(login.id())
                .password(login.password())
                .email(info.getEmail())
                .name(info.getName())
                .build();

        return saveUser(userEntityRepository, new UserEntity(command)).getId();
    }
}
