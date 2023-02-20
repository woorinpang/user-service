package com.wooringpang.userservice.domain.user.service;

import com.wooringpang.userservice.domain.user.dto.SaveUserParam;
import com.wooringpang.userservice.domain.user.dto.UpdateUserParam;
import com.wooringpang.userservice.domain.user.dto.UserListDto;
import com.wooringpang.userservice.domain.user.dto.UserSearchCondition;
import com.wooringpang.userservice.domain.user.entity.User;
import com.wooringpang.userservice.domain.user.repository.UserQueryRepository;
import com.wooringpang.userservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 유저 목록을 조회하여 페이지와 함께 반환한다.
     */
    public Page<UserListDto> findUsers(UserSearchCondition condition, Pageable pageable) {
        return userQueryRepository.findUsers(condition, pageable);
    }

    /**
     * 유저 단건 조회하여 반환한다.
     */
    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());
    }

    /**
     * 사용자 정보를 받아 등록하고 userId를 반환한다.
     */
    @Transactional
    public Long save(SaveUserParam param) {
        return userRepository.save(param.toEntity(passwordEncoder)).getId();
    }

    /**
     * 사용자 정보를 받아 수정한다.
     */
    public void update(Long userId, UpdateUserParam param) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없음"));

        //새로운 비밀번호가 들어오면 인코드 아니면 기존 비밀번호 업데이트
        param.encodePassword(hasText(param.getPassword()) ? passwordEncoder.encode(param.getPassword()) : findUser.getPassword());
        findUser.update(param);
    }

    /**
     * 사용자 refresh token 정보를 받아 수정하고 권한 정보를 반환한다.
     */
    @Transactional
    public String updateRefreshToken(Long userId, String updateRefreshToken) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("없음"));

        findUser.updateRefreshToken(updateRefreshToken);
        return findUser.getRole().getCode();
    }

    /**
     * 토큰으로 사용자를 찾아 반환한다.
     */
    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UsernameNotFoundException("없음"));
    }

    /**
     * 로그인아이디로 사용자를 찾아 반환한다.
     */
    public User findBySignId(String signId) {
        return userRepository.findBySignId(signId)
                .orElseThrow(() -> new UsernameNotFoundException("없음"));
    }

    /**
     * 이메일로 사용자를 찾아 반환한다.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("없음"));
    }

    /**
     * 모든 사용자를 생성일 역순으로 정렬 조회하여 반환한다.
     */
    public List<UserListDto> findAllDesc() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate")).stream()
                .map(UserListDto::new)
                .collect(Collectors.toList());
    }
}
