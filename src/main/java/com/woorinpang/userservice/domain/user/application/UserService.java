package com.woorinpang.userservice.domain.user.application;

import com.woorinpang.userservice.domain.user.application.dto.UserCommandMapper;
import com.woorinpang.userservice.domain.user.application.dto.command.UserJoinCommand;
import com.woorinpang.userservice.domain.user.application.dto.command.UserUpdateInfoCommand;
import com.woorinpang.userservice.domain.user.exception.PasswordNotMatchException;
import com.woorinpang.userservice.domain.user.infrastructure.dto.UserSearchCondition;
import com.woorinpang.userservice.domain.user.application.dto.command.SaveUserCommand;
import com.woorinpang.userservice.domain.user.application.dto.command.UpdateUserCommand;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
import com.woorinpang.userservice.domain.user.exception.EmailAlreadyExistsException;
import com.woorinpang.userservice.domain.user.exception.UserNotFoundException;
import com.woorinpang.userservice.domain.user.exception.UsernameAlreadyExistsException;
import com.woorinpang.userservice.domain.user.infrastructure.UserQueryRepository;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.domain.user.infrastructure.dto.FindPageUserDto;
import com.woorinpang.userservice.domain.user.presentation.request.SocialUserResponse;
import com.woorinpang.userservice.domain.user.presentation.user.request.UserLeaveRequest;
import com.woorinpang.userservice.global.exception.BusinessMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserCommandMapper mapper;

    /**
     * 사용자 목록조회
     */
    public Page<FindPageUserDto> findUsers(UserSearchCondition condition, Pageable pageable) {
        return userQueryRepository.findPageUsers(condition, pageable);
    }

    /**
     * 사용자 단건조회
     */
    public User findUser(Long userId) {
        return this.findById(userId);
    }

    /**
     * 사용자 저장
     */
    @Transactional
    public Long saveUser(SaveUserCommand command) {
        checkDuplicateUsername(command.username());
        checkDuplicateEmail(command.email());
        return userRepository.save(mapper.toUser(command)).getId();
    }

    /**
     * 사용자 수정
     */
    @Transactional
    public void updateUser(Long userId, UpdateUserCommand command) {
        checkDuplicateEmail(command.email());
        this.findById(userId).update(command);
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteUser(Long userId) {
        User findUser = this.findById(userId);
        findUser.updateUserStateCode(UserState.DELETE);
    }

    /**
     * 사용자 회원가입
     */
    @Transactional
    public Long join(UserJoinCommand command) {
        checkDuplicateUsername(command.username());
        checkDuplicateEmail(command.email());

        //TODO Social Login
        if (command.isProvider()) {

        }
        return userRepository.save(mapper.toUser(command)).getId();
    }

    /**
     * 사용자 정보 조회
     */
    public User findInfo(Long userId) {
        return this.findById(userId);
    }

    /**
     * 사용자 정보 수정
     */
    @Transactional
    public void updateInfo(Long userId, UserUpdateInfoCommand command) {
        checkDuplicateEmail(command.email());
        this.findById(userId).updateInfo(command);
    }

    /**
     * 사용자 비밀번호 확인
     */
    public Boolean matchPassword(String username, String password) {
        try {
            this.findUserVerifyPassword(username, password);
        } catch (PasswordNotMatchException e) {
            log.error("error message = {}", e);
            return false;
        }
        return true;
    }

    /**
     * 사용자 아이디 중복확인
     */
    public Boolean existsUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * 사용자 회원탈퇴
     */
    @Transactional
    public Boolean leave(String username, UserLeaveRequest request) {
        User entity = findUserVerify(username, request);
        entity.updateUserStateCode(UserState.LEAVE);
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 모든 사용자를 생성일 역순으로 정렬 조회하여 반환한다.
     */
    public List<FindPageUserDto> findAllDesc() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate")).stream()
                .map(FindPageUserDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 이메일 중복 확인
     */
    public Boolean existsEmail(String email, String username) {
        if (!hasText(email)) {
            throw new IllegalArgumentException("이메일 없음");
        }

        if (!hasText(username)) {
            return userRepository.findByEmail(email).isPresent();
        } else {
            return userRepository.findByEmailAndUsernameNot(email, username).isPresent();
        }
    }

    public SocialUserResponse getSocialUserInfo(String provider, String token) {
        SocialUserResponse social = null;
        switch (provider) {
            case "google":
                social = getGoogleUserInfo(token);
                break;
            case "naver":
                social = getNaverUserInfo(token);
                break;
            case "kakao":
                social = getKakaoUserInfo(token);
                break;
            default:
                break;
        }

        if (social == null) throw new IllegalArgumentException("소셜 없음");

        return social;
    }

    private SocialUserResponse getGoogleUserInfo(String token) {
        return null;
    }

    private SocialUserResponse getNaverUserInfo(String token) {
        return null;
    }

    private SocialUserResponse getKakaoUserInfo(String token) {
        return null;
    }

    /**
     * 소셜 사용자 엔티티 조회
     */
    private User findSocialUser(String providerCode, String providerId) {
        Optional<User> user;

        //공급자 id 로 조회
        switch (providerCode) {
            case "google":
                user = userRepository.findByGoogleId(providerId);
                break;
            case "naver":
                user = userRepository.findByNaverId(providerId);
                break;
            case "kakao":
                user = userRepository.findByKakaoId(providerId);
                break;
            default:
                user = Optional.empty();
                break;
        }

        return user.orElse(null);
    }




    public User findUserVerify(String username, UserLeaveRequest request) {
        if (!hasText(username)) {
            throw new BusinessMessageException("에러");
        }

        User user = null;

        if ("password".equals(request.getProvider())) {
            user = findUserVerifyPassword(username, request.getPassword());
        } else {
            user = findSocialUserByToken(request.getProvider(), request.getToken());

            if (user == null) {
                throw new BusinessMessageException("없음");
            }

            if (!username.equals(user.getUsername())) {
                throw new BusinessMessageException("ㄴㄴㄴㄴ");
            }
        }
        return user;
    }



    private User findSocialUserByToken(String provider, String token) {
        SocialUserResponse response = getSocialUserInfo(provider, token);
        return findSocialUser(provider, response.getId());
    }

    /**
     * User 단건 조회
     */
    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    /**
     * Username 중복확인
     */
    private void checkDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) throw new UsernameAlreadyExistsException(username);
    }

    /**
     * Email 중복확인
     */
    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) throw new EmailAlreadyExistsException(email);
    }

    /**
     * 사용자 조회, 비밀번호 검증
     */
    private User findUserVerifyPassword(String username, String password) {
        User findUser = this.findByUsername(username);
        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new PasswordNotMatchException();
        }
        return findUser;
    }

    /**
     * 로그인아이디로 사용자를 찾아 반환한다.
     */
    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username=%s은 존재하지 않는 사용자입니다.".formatted(username)));
    }


}
