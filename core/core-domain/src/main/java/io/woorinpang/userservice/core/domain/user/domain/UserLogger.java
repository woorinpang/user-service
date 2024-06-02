package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.user.repository.UserLoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLogger {
    private final UserLoginLogRepository userLoginLogRepository;

    public void log(UserLoginLogCommand command) {
        UserLoginLog userLoginLogEntity = new UserLoginLog(command);
        userLoginLogRepository.save(userLoginLogEntity);
    }
}
