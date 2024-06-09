package io.woorinpang.userservice.admin.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;

    public long findUsers(long userId) {
        return adminUserRepository.findById(userId).get().getId();
    }
}
