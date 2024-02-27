package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.storage.core.db.user.UserLoginLogEntity;
import io.woorinpang.userservice.storage.core.db.user.UserLoginLogRepository;
import io.woorinpang.userservice.storage.core.db.user.dto.UserLoginLogCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLogger {
    private final UserLoginLogRepository userLoginLogRepository;

    public void log(UserLoginLogCommand command) {
        UserLoginLogEntity userLoginLogEntity = new UserLoginLogEntity(command);
        userLoginLogRepository.save(userLoginLogEntity);
    }
}
