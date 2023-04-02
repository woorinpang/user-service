package com.woorinpang.userservice.test;

import com.woorinpang.userservice.domain.auth.application.AuthService;
import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    //@Autowired
    //UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {

//        User user = User.createBuilder()
//                .username("spring1")
//                .password("1234")
//                .email("spring1@naver.com")
//                .name("스프링")
//                .role(Role.ADMIN)
//                .userState(UserState.NORMAL)
//                .build();
//
//        userRepository.save(user);

        // 로그인 유저의 권한 목록 주입
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.getCode()));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUser.username(), customUser.password(), authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
