package com.woorinpang.userservice.core.user.service;

import com.woorinpang.userservice.core.user.domain.User;
import com.woorinpang.userservice.core.user.domain.UserState;
import com.woorinpang.userservice.core.user.domain.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {

    private UserRepository userRepository;

    /**
     * SecurityConfig > configure > UserDetailsService 메소드에서 호출된다.
     * 스프링 시큐리티에 의해 로그인 대상 사용자의 패스워드와 권한 정보를 DB 에서 조회하여 UserDetails 를 반환한다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername! email = {}", username);
        //로그인 실패시 이메일 계정을 로그에 남기기 위해 세팅하고 unsuccessfulAuthentication 메소드에서 받아서 로그에 입력한다.
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("username", username);

        //UsernameNotFoundException 을 던지면 AbstractUserDetailsAuthenticationProvider 에서 BadCredentialsException 으로 처리하기 때문에 IllegalArgumentException 을 발생시킨다.
        //사용자가 없는 것인지 패스워드가 잘못된 것인지 구분하기 위함이다.
        User findUser = userRepository.findByUsername(username)
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
        return new org.springframework.security.core.userdetails.User(findUser.getUsername(), findUser.getPassword(), authorities);
    }
}
