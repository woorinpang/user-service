package io.woorinpang.userservice.core.api.config;

import io.woorinpang.userservice.core.domain.support.error.CoreDomainException;
import io.woorinpang.userservice.core.domain.support.error.DomainErrorType;
import io.woorinpang.userservice.core.domain.user.application.AuthService;
import io.woorinpang.userservice.core.domain.user.domain.FindUser;
import io.woorinpang.userservice.core.domain.user.domain.UserState;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WoorinpangLogin implements UserDetailsService {
    private final AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername! username={}", email);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("email", email);

        FindUser findUser = authService.findUser(email);

        log.info("{} 사용자 존재함", findUser);

        if (!UserState.NORMAL.equals(findUser.getUserState())) {
            throw new CoreDomainException(DomainErrorType.USER_STATE_IS_NOT_NORMAL);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(findUser.getUserRole().getCode()));

        return new org.springframework.security.core.userdetails.User(
                findUser.getEmail(),
                findUser.getPassword(),
                authorities
        );
    }
}
