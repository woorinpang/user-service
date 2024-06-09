package io.woorinpang.userservice.admin.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;
    private final AdminUserQueryRepository adminUserQueryRepository;

    @Transactional(readOnly = true)
    public Page<AdminUser> findUsers(AdminUserSearchCondition condition, Pageable pageable) {
        return adminUserQueryRepository.findUsers(condition, pageable);
    }
}
