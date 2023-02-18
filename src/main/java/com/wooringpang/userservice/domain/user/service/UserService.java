package com.wooringpang.userservice.domain.user.service;

import com.wooringpang.userservice.domain.user.dto.UserSearchCondition;
import com.wooringpang.userservice.domain.user.entity.User;
import com.wooringpang.userservice.domain.user.repository.UserQueryRepository;
import com.wooringpang.userservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;

    /**
     * 유저 목록 조회
     */
    public Page<User> findUsers(UserSearchCondition condition, Pageable pageable) {
        return userQueryRepository.findUsers(condition, pageable);
    }

}
