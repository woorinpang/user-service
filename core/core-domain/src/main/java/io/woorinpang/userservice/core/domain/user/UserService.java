package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserQueryRepository;
import io.woorinpang.userservice.core.db.user.UserRepository;
import io.woorinpang.userservice.core.db.user.dto.FindPageUserResponse;
import io.woorinpang.userservice.core.domain.user.dto.UserCommandMapper;
import io.woorinpang.userservice.core.domain.user.dto.UserSearchParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

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
    private final UserCommandMapper mapper;

    /**
     * 사용자 목록조회
     */
    public Page<FindPageUserResponse> findPageUser(UserSearchParam param, Pageable pageable) {
        return userQueryRepository.findPageUser(param.toCondition(), pageable);
    }

    /**
     * 사용자 단건조회
     */
    public UserTemp findUser(Long userId) {
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
        UserTemp findUser = this.findById(userId);
        findUser.updateUserStateCode(UserState.DELETE);
    }

    /**
     * 사용자 회원가입
     */
    @Transactional
    public Long join(UserJoinCommand command) {
        checkDuplicateUsername(command.username());
        checkDuplicateEmail(command.email());


        return userRepository.save(mapper.toUser(command)).getId();
    }

    /**
     * 사용자 정보 조회
     */
    public UserTemp findInfo(Long userId) {
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
        UserTemp findUser = findUserVerify(username, command);
        findUser.updateUserStateCode(UserState.LEAVE);
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 사용자 검증 및 조회
     */
    private UserTemp findUserVerify(String username, UserLeaveCommand command) {
        if (!hasText(username)) {
            throw new BusinessMessageException("로그인이 필요합니다.");
        }

        UserTemp user = null;

        if (Provider.WOORINPANG.equals(command.provider())) {
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
    private UserTemp findById(Long userId) {
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
    private UserTemp findUserVerifyPassword(String username, String password) {
        UserTemp findUser = this.findByUsername(username);
        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new PasswordNotMatchException();
        }
        return findUser;
    }

    /**
     * 로그인아이디로 사용자를 찾아 반환한다.
     */
    private UserTemp findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username=%s은 존재하지 않는 사용자입니다.".formatted(username)));
    }

    /**
     * 사용자 단건 조회 By Token
     */
    private UserTemp findSocialUserByToken(Provider provider, String token) {
        SocialUserCommand command = getSocialUserInfo(provider, token);
        return findSocialUser(provider, command.id());
    }

    private SocialUserCommand getSocialUserInfo(Provider provider, String token) {
        SocialUserCommand social = null;
        switch (provider) {
            case Provider.GOOGLE:
                social = getGoogleUserInfo(token);
                break;
            case Provider.NAVER:
                social = getNaverUserInfo(token);
                break;
            case Provider.KAKAO:
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
    private UserTemp findSocialUser(Provider provider, String providerId) {
        //공급자 id 로 조회
        Optional<UserTemp> user = switch (provider) {
            case Provider.GOOGLE -> userRepository.findByGoogleId(providerId);
            case Provider.NAVER -> userRepository.findByNaverId(providerId);
            case Provider.KAKAO -> userRepository.findByKakaoId(providerId);
            default -> null;
        };
        return user.orElse(null);
    }
}
