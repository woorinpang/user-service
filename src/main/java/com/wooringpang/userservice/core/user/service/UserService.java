package com.wooringpang.userservice.core.user.service;

import com.wooringpang.userservice.core.log.repository.LoginLogRepository;
import com.wooringpang.userservice.core.user.presentation.request.SocialUserResponse;
import com.wooringpang.userservice.core.user.presentation.request.UserFindPasswordSaveRequest;
import com.wooringpang.userservice.core.user.presentation.request.UserLoginRequest;
import com.wooringpang.userservice.core.user.dto.*;
import com.wooringpang.userservice.core.user.domain.User;
import com.wooringpang.userservice.core.user.domain.UserState;
import com.wooringpang.userservice.core.user.repository.UserQueryRepository;
import com.wooringpang.userservice.core.user.repository.UserRepository;
import com.wooringpang.userservice.core.user.service.param.JoinUserParam;
import com.wooringpang.userservice.core.user.service.param.SaveUserParam;
import com.wooringpang.userservice.core.user.service.param.UpdateUserParam;
import com.wooringpang.userservice.global.exception.BusinessMessageException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final LoginLogRepository loginLogRepository;
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
    public String updateRefreshToken(String  signId, String updateRefreshToken) {
        User findUser = userRepository.findBySignId(signId)
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

    /**
     * SecurityConfig > configure > UserDetailsService 메소드에서 호출된다.
     * 스프링 시큐리티에 의해 로그인 대상 사용자의 패스워드와 권한 정보를 DB 에서 조회하여 UserDetails 를 반환한다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername! email = {}", email);
        //로그인 실패시 이메일 계정을 로그에 남기기 위해 세팅하고 unsuccessfulAuthentication 메소드에서 받아서 로그에 입력한다.
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("email", email);

        //UsernameNotFoundException 을 던지면 AbstractUserDetailsAuthenticationProvider 에서 BadCredentialsException 으로 처리하기 때문에 IllegalArgumentException 을 발생시킨다.
        //사용자가 없는 것인지 패스워드가 잘못된 것인지 구분하기 위함이다.
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        log.info("{} 사용자 존재함", findUser);

        if (!UserState.NORMAL.equals(findUser.getUserState())) {
            throw new IllegalArgumentException("로그인할수 없습니다.");
        }

        //로그인 유저의 권한 목록 주입
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(findUser.getRole().getCode()));

//        if (findUser.isSocialUser() && !hasText(findUser.getPassword())) {
//            //소셜 회원이고 비밀번호가 등록되지 않은 경우
//            return new SocialUser(findUser.getEmail(), authorities);
//        } else {
//            return new org.springframework.security.core.userdetails.User(findUser.getEmail(), findUser.getPassword(), authorities);
//        }
        return new org.springframework.security.core.userdetails.User(findUser.getEmail(), findUser.getPassword(), authorities);
    }

    /**
     * 로그인 후처리
     */
    @Transactional
    public void loginCallback(String email, Boolean isSuccess, String failContent) {
        User findUser = userRepository.findByEmail(email)
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
    public Boolean existsEmail(String email, String signId) {
        if (!hasText(email)) {
            throw new IllegalArgumentException("이메일 없음");
        }

        if (!hasText(signId)) {
            return userRepository.findByEmail(email).isPresent();
        } else {
            return userRepository.findByEmailAndSignIdNot(email, signId).isPresent();
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
}
