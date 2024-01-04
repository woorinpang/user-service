package io.woorinpang.userservice.config;

import io.woorinpang.userservice.core.domain.user.FindUser;
import io.woorinpang.userservice.core.domain.user.service.AuthService;
import io.woorinpang.userservice.core.enums.user.UserState;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername! username={}", username);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("username", username);

        FindUser findUser = authService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        log.info("{} 사용자 존재함", findUser);

        if (!UserState.NORMAL.equals(findUser.getUserState())) {
            throw new IllegalArgumentException("UserState 가 NORMAL 이 아닙니다.");
        }

        // 로그인 유저의 권한 목록 주입
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(findUser.getUserRole().getCode()));

        return new org.springframework.security.core.userdetails.User(findUser.getUsername(), findUser.getPassword(), authorities);
    }
}
