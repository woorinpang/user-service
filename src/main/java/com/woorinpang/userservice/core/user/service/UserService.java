package com.woorinpang.userservice.core.user.service;

import com.woorinpang.common.exception.BusinessMessageException;
import com.woorinpang.userservice.core.log.repository.LoginLogRepository;
import com.woorinpang.userservice.core.user.domain.*;
import com.woorinpang.userservice.core.user.dto.UserListDto;
import com.woorinpang.userservice.core.user.dto.UserSearchCondition;
import com.woorinpang.userservice.core.user.infrastructure.UserFindPasswordQueryRepository;
import com.woorinpang.userservice.core.user.infrastructure.UserQueryRepository;
import com.woorinpang.userservice.core.user.presentation.request.*;
import com.woorinpang.userservice.core.user.service.param.JoinUserParam;
import com.woorinpang.userservice.core.user.service.param.SaveUserParam;
import com.woorinpang.userservice.core.user.service.param.UpdateUserParam;
import com.woorinpang.userservice.global.config.UserPasswordChangeEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final UserFindPasswordRepository userFindPasswordRepository;
    private final UserFindPasswordQueryRepository userFindPasswordQueryRepository;
    private final LoginLogRepository loginLogRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    private final JavaMailSender javaMailSender;
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
                .orElseThrow(() -> new BusinessMessageException("foo"));
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
    public String updateRefreshToken(String username, String updateRefreshToken) {
        User findUser = userRepository.findByUsername(username)
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
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
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

    /**
     * 로그인 후처리
     */
    @Transactional
    public void loginCallback(String email, Boolean isSuccess, String failContent) {
        User findUser = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException("없음"));

        if (Boolean.TRUE.equals(isSuccess)) {
            findUser.successLogin();
        } else {
            findUser.failLogin();
        }
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

    /**
     * 사용자 회원 가입
     */
    @Transactional
    public Long join(JoinUserParam param) {
        Boolean exists = this.existsEmail(param.getEmail(), null);
        if (exists) {
            throw new IllegalArgumentException("이미 이메일이 존재함");
        }

//        if (param.isProvider()) {
//
//        }
        return userRepository.save(param.toEntity(passwordEncoder)).getId();
    }

    /**
     * 사용자 비밀번호 찾기 유효성 확인
     */
    @Transactional
    public Boolean validPassword(String tokenValue) {
        if (!hasText(tokenValue)) {
            throw new IllegalArgumentException("invalid");
        }

        Optional<UserFindPassword> findUserFindPassword = userFindPasswordRepository.findByTokenValue(tokenValue);
        if (findUserFindPassword.isPresent()) {
            UserFindPassword userFindPassword = findUserFindPassword.get();

            boolean isExpired = LocalDateTime.now().isAfter(userFindPassword.getCreatedDate().plusHours(1));//1시간 후 만료
            if(Boolean.FALSE.equals(userFindPassword.isChange()) && !isExpired) return true;
        }
        return false;
    }

    /**
     * 사용자 비밀번호 찾기 변경
     */
    @Transactional
    public Boolean changePassword(UserFindPasswordUpdateRequest request) {
        final String tokenValue = request.getTokenValue();

        UserFindPassword findUserFindPassword = userFindPasswordRepository.findByTokenValue(tokenValue)
                .orElseThrow(() -> new BusinessMessageException("없음"));

        if (Boolean.TRUE.equals(findUserFindPassword.isChange()) || LocalDateTime.now().isAfter(findUserFindPassword.getCreatedDate().plusHours(1))) {
            throw new BusinessMessageException("에러");
        }

        User findUser = userRepository.findByEmail(findUserFindPassword.getUserFindPasswordId().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("없음"));

        findUser.updatePassword(passwordEncoder.encode(request.getPassword()));

        findUserFindPassword.updateIsChange(Boolean.TRUE);

        return true;
    }

    /**
     * 사용자 비밀번호 변경
     */
    @Transactional
    public Boolean updatePassword(String username, UserPasswordUpdateRequest request) {
        try {
            User user = this.findUserVerify(username, request);

            user.updatePassword(passwordEncoder.encode(request.getPassword()));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw e;
        }
        return true;
    }

    /**
     * 사용자 비밀번호 확인
     */
    public Boolean matchPassword(String username, String password) {
        try {
            this.findUserVerifyPassword(username, password);
        } catch (BusinessMessageException e) {
            return false;
        }
        return true;
    }

    /**
     * 사용자 username 으로 조회
     */
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessMessageException("없음"));
    }

    /**
     * OAuth 사용자 검색
     */
    public User loadUserBySocial(UserLoginRequest request) {
        SocialUserResponse response = this.getSocialUserInfo(request.getProvider(), request.getToken());

        User findUser = this.findSocialUser(request.getProvider(), response.getId());

        if (findUser == null) {
            throw new IllegalArgumentException("없음");
        }
        if (!UserState.NORMAL.equals(findUser.getUserState())) {
            throw new IllegalArgumentException("주의");
        }

        return findUser;
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

    /**
     * 사용자 비밀번호 찾기
     */
    public Boolean findPassword(UserFindPasswordSaveRequest request) {
        String email = request.getEmail();

        User findUser = userRepository.findByEmailAndUsername(email, request.getUserName())
                .orElseThrow(() -> new BusinessMessageException("없음"));

        //이메일 전송
        try {
            final String mainUrl = request.getMainUrl();
            final String tokenValue = UUID.randomUUID().toString().replaceAll("-", "");

            final String subjec = "이메일 유저 패스워드 타이틀";
            final String text = UserPasswordChangeEmail.html;
            final String username = findUser.getUsername();
            final String changePasswordUrl = request.getChangePasswordUrl() + "?token=" + tokenValue;

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject(subjec);
            helper.setText(String.format(text, mainUrl, username, changePasswordUrl), true); //String.format 에서 %를 쓰려면 %%로

            log.info("start send change password email: " + email);
            javaMailSender.send(message);

            Integer requestNo = userFindPasswordQueryRepository.findNextRequestNo(email);
            UserFindPassword userFindPassword = request.toEntity(requestNo, tokenValue);

            userFindPasswordRepository.save(userFindPassword);

            log.info("end send change password email - email: {}, tokenValue: {}", email, tokenValue);
        } catch (MessagingException e) {
            String errorMessage = "error.user.find.password";
            log.error(errorMessage + ": " + e.getMessage());
            throw new BusinessMessageException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "error.user.find.password";
            log.error(errorMessage + ": " + e.getMessage());
            throw new BusinessMessageException(errorMessage);
        }
        return true;
    }

    private User findUserVerifyPassword(String username, String password) {
        User entity = this.findByUsername(username);
        if (!passwordEncoder.matches(password, entity.getPassword())) {
            throw new BusinessMessageException("틀려");
        }
        return entity;
    }

    /**
     * 사용자 정보 수정
     */
    @Transactional
    public String updateInfo(String username, UserUpdateInfoRequest request) {
        User user = findUserVerify(username, request);

        user.updateInfo(request.getUserName(), request.getEmail());
        return user.getUsername();
    }

    private User findUserVerify(String username, UserVerifyRequest request) {
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

    @Transactional
    public Boolean leave(String username, UserVerifyRequest request) {
        User entity = findUserVerify(username, request);
        entity.updateUserStateCode(UserState.LEAVE);
        return true;
    }

    public Boolean deleteUser(Long userId) {
        User findUser = this.findUser(userId);
        findUser.updateUserStateCode(UserState.DELETE);
        return true;
    }
}
