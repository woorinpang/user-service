package com.woorinpang.userservice.domain.user.application;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.woorinpang.userservice.domain.user.application.dto.UserCommandMapper;
import com.woorinpang.userservice.domain.user.application.dto.command.*;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
import com.woorinpang.userservice.domain.user.exception.EmailAlreadyExistsException;
import com.woorinpang.userservice.domain.user.exception.PasswordNotMatchException;
import com.woorinpang.userservice.domain.user.exception.UserNotFoundException;
import com.woorinpang.userservice.domain.user.exception.UsernameAlreadyExistsException;
import com.woorinpang.userservice.domain.user.infrastructure.UserQueryRepository;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.domain.user.infrastructure.dto.FindPageUserDto;
import com.woorinpang.userservice.domain.user.infrastructure.dto.UserSearchCondition;
import com.woorinpang.userservice.global.common.entity.Provider;
import com.woorinpang.userservice.global.exception.BusinessMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    /**
     * 구글 클라이언트 ID
     */
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    /**
     * 카카오 사용자 정보 URL
     */
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URI;

    /**
     * 네이버 사용자 정보 URL
     */
    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String NAVER_USER_INFO_URI;


    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserCommandMapper mapper;
    private final RestTemplate restTemplate;

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
    public Boolean leave(String username, UserLeaveCommand command) {
        User findUser = findUserVerify(username, command);
        findUser.updateUserStateCode(UserState.LEAVE);
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 사용자 검증 및 조회
     */
    public User findUserVerify(String username, UserLeaveCommand command) {
        if (!hasText(username)) {
            throw new BusinessMessageException("로그인이 필요합니다.");
        }

        User user = null;

        if ("password".equals(command.provider())) {
            user = findUserVerifyPassword(username, command.password());
        } else {
            user = findSocialUserByToken(command.provider(), command.token());

            if (user == null) {
                throw new BusinessMessageException("사용자 정보가 올바르지 않습니다.");
            }

            if (!username.equals(user.getUsername())) {
                throw new BusinessMessageException("사용자 정보가 올바르지 않습니다.");
            }
        }
        return user;
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

    /**
     * 사용자 단건 조회 By Token
     */
    private User findSocialUserByToken(Provider provider, String token) {
        SocialUserCommand command = getSocialUserInfo(provider, token);
        return findSocialUser(provider, command.id());
    }

    public SocialUserCommand getSocialUserInfo(Provider provider, String token) {
        SocialUserCommand social = null;
        switch (provider) {
            case GOOGLE:
                social = getGoogleUserInfo(token);
                break;
            case NAVER:
                social = getNaverUserInfo(token);
                break;
            case KAKAO:
                social = getKakaoUserInfo(token);
                break;
            default:
                break;
        }

        if (social == null) throw new IllegalArgumentException("소셜 없음");

        return social;
    }

    /**
     * 구글 사용자 정보 조회
     */
    private SocialUserCommand getGoogleUserInfo(String token) {
        try {
            NetHttpTransport transport = new NetHttpTransport();
            GsonFactory gsonFactory = new GsonFactory();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                log.info("google oauth2: {}", payload.toString());

                return SocialUserCommand.builder()
                        .id(payload.getSubject())
                        .email(payload.getEmail())
                        .name((String) payload.get("name"))
                        .build();
            }

            return null;
        } catch (GeneralSecurityException e) {
            log.error("GeneralSecurityException = {}", e);
            throw new BusinessMessageException("사용자 유저가 아닙니다.");
        } catch (IOException e) {
            log.error("IOException = {}", e);
            throw new BusinessMessageException("사용자 유저가 아닙니다.");
        } catch (Exception e) {
            log.error("Exception = {}", e);
            throw new BusinessMessageException("사용자 유저가 아닙니다.");
        }
    }

    /**
     * 네이버 사용자 정보 조회
     */
    private SocialUserCommand getNaverUserInfo(String token) {
        //TODO
        return null;
    }

    private SocialUserCommand getKakaoUserInfo(String token) {
        //TODO
        return null;
    }

    /**
     * 소셜 사용자 엔티티 조회
     */
    private User findSocialUser(Provider provider, String providerId) {
        //공급자 id 로 조회
        Optional<User> user = switch (provider) {
            case GOOGLE -> userRepository.findByGoogleId(providerId);
            case NAVER -> userRepository.findByNaverId(providerId);
            case KAKAO -> userRepository.findByKakaoId(providerId);
            default -> null;
        };
        return user.orElse(null);
    }
}
