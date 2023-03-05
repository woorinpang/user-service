package com.woorinpang.userservice.core.user.service;

import com.woorinpang.common.exception.BusinessMessageException;
import com.woorinpang.userservice.core.user.domain.User;
import com.woorinpang.userservice.core.user.domain.UserState;
import com.woorinpang.userservice.core.user.dto.UserListDto;
import com.woorinpang.userservice.core.user.dto.UserSearchCondition;
import com.woorinpang.userservice.core.user.presentation.request.*;
import com.woorinpang.userservice.core.user.repository.UserQueryRepository;
import com.woorinpang.userservice.core.user.repository.UserRepository;
import com.woorinpang.userservice.core.user.service.param.JoinUserParam;
import com.woorinpang.userservice.core.user.service.param.UpdateUserParam;
import com.woorinpang.userservice.core.log.repository.LoginLogRepository;
import com.woorinpang.userservice.core.user.service.param.SaveUserParam;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
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
    private final LoginLogRepository loginLogRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

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
        //TODO 하던중
        return false;
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
        final String email = request.getEmail();

        Optional<User> findUser = userRepository.findByEmailAndName(email, request.getUserName());
        if (!findUser.isPresent()) {
            throw new BusinessMessageException("없음");
        }

        User entity = findUser.get();

        //TODO 이메일 전송
        /*try {
            final String mainUrl = request.getMailUrl();
            String tokenValue = UUID.randomUUID().toString().replaceAll("-", "");

            String subject = "email.user.password.title";

        }*/
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
