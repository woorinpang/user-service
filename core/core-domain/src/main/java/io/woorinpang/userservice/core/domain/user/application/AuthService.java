package io.woorinpang.userservice.core.domain.user.application;

import io.woorinpang.userservice.core.domain.user.domain.*;
import io.woorinpang.userservice.core.domain.user.domain.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserFinder userFinder;
    private final UserAppender userAppender;
    private final UserModifier userModifier;
    private final UserValidator userValidator;
    private final UserLogger userLogger;

    public boolean existsEmail(String email) {
        return userFinder.findByEmail(email).isPresent();
    }

    public long userJoin(LoginUser login, String name, Provider provider) {
        userValidator.validEmail(login.email());

        return userAppender.append(login, name, provider);
    }

    public Optional<FindUser> findUser(String email) {
        return userFinder.findByEmail(email)
                .map(FindUser::new);
    }

    @Transactional
    public void loginCallback(Long siteId, String remoteIp, String email,  boolean success, String failContent) {
        FindUser findUser = this.findUser(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found in the database!!"));

        if (Boolean.TRUE.equals(success)) {
            userModifier.successLogin(new UserTarget(findUser.getId()));
        } else {
            userModifier.failLogin(new UserTarget(findUser.getId()));
        }

        // 로그인 로그 입력
        UserLoginLogCommand command = UserLoginLogCommand.builder()
                .siteId(siteId)
                .email(email)
                .provider(findUser.getProvider())
                .remoteIp(remoteIp)
                .success(success)
                .failContent(failContent)
                .build();
        userLogger.log(command);
    }

    public boolean isAuthorization(String email, List<String> roles) {
        User findUser = userFinder.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found in the database!!"));
        return roles.contains(findUser.getRole().getCode());
    }

    public FindUser loadUserBySocial(String email, String name, Provider provider) {
        UserTarget.UserTargetBuilder targetBuilder = UserTarget.builder();

        userValidator.validEmailAndProvider(new UserEmailWithProvider(email, provider));

        userFinder.findByEmail(email).ifPresentOrElse(user -> {
            targetBuilder.id(user.getId());

        }, () -> {
            long appendedId = userAppender.append(new LoginUser(email, null), name, provider);
            targetBuilder.id(appendedId);
        });

        return userFinder.findUser(targetBuilder.build());
    }
}
