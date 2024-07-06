package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.storage.db.core.user.UserLoginLog;
import io.woorinpang.userservice.storage.db.core.user.UserLoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserLogger {
    private final UserLoginLogRepository userLoginLogRepository;

    @Transactional
    public void log(UserLoginLogCommand log) {
        userLoginLogRepository.save(new UserLoginLog(log));
    }
}
