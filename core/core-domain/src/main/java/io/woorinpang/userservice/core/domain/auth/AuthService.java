package io.woorinpang.userservice.core.domain.auth;

import io.woorinpang.userservice.core.db.log.UserLoginLogEntityRepository;
import io.woorinpang.userservice.core.db.user.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    private final UserLoginLogEntityRepository userLoginLogRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername! username={}", username);
        // 로그인 실패시 이메일 계정을 로그에 남기기 위해 세팅하고 unsuccessfulAuthentication 메소드에서 받아서 로그에 입력한다.
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("username", username);

        // UsernameNotFoundException 을 던지면 AbstractUserDetailsAuthenticationProvider 에서 BadCredentialsException 으로 처리하기 때문에 IllegalArgumentException 을 발생시켰다.
        // 사용자가 없는 것인지 패스워드가 잘못된 것인지 구분하기 위함이다.
        UserTemp findUser = this.findByUsername(username);
        log.info("{} 사용자 존재함", findUser);

        if (!UserState.NORMAL.equals(findUser.getUserState())) {
            throw new IllegalArgumentException("UserState 가 NORMAL 이 아닙니다.");
        }

        // 로그인 유저의 권한 목록 주입
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(findUser.getRole().getCode()));

        if (findUser.isSocialUser() && !hasText(findUser.getPassword())) { // 소셜 회원이고 비밀번호가 등록되지 않은 경우
            return new SocialUser(findUser.getUsername(), authorities);
        } else {
            return new org.springframework.security.core.userdetails.User(findUser.getUsername(), findUser.getPassword(), authorities);
        }
    }

    @Transactional
    public void loginCallback(Long siteId, String username, Boolean isSuccess, String failContent) {
        UserTemp user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));

        if (Boolean.TRUE.equals(isSuccess)) {
            user.successLogin();
        } else {
            user.failLogin();
        }

        // 로그인 로그 입력
        userLoginLogRepository.save(
                UserLoginLog.createUserLoginLog()
                        .siteId(siteId)
                        .username(username)
                        .remoteIp("192.168.0.1")
                        .isSuccess(isSuccess)
                        .failContent(failContent)
                        .build()
        );
    }

    public UserTemp findUserByUsername(String username) {
        return this.findByUsername(username);
    }

    /**
     * 사용자 refresh token 정보를 필드에 입력한다
     */
    @Transactional
    public String updateRefreshToken(Long userId, String updateRefreshToken) {

        UserTemp findUser = userEntityRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId)).updateRefreshToken(updateRefreshToken);

        return findUser.getRole().getCode();
    }

    /**
     * 토큰으로 사용자를 찾아 반환한다.
     */
    public UserTemp findByRefreshToken(String refreshToken) {
        return userEntityRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UsernameNotFoundException("notexists"));
    }

    private UserTemp findByUsername(String username) {
        return userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("nonononono"));
    }

    public Boolean isAuthorization(String username, String httpMethod, String requestPath) {
        return userEntityRepository.findByUsername(username).isPresent();
    }

    public Boolean isAuthorization(HttpServletRequest request, Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString).collect(Collectors.toList());
        //TODO Authorization table에서 권한을 가져오고 검증해서 값을 Boolean 으로 반환한다.
        return true;
    }
}
