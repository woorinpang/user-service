package io.woorinpang.userservice.core.domain.user.application;

import io.woorinpang.userservice.core.domain.user.domain.*;
import io.woorinpang.userservice.core.domain.user.domain.Provider;
import lombok.RequiredArgsConstructor;
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
        return userValidator.existsEmail(email);
    }

    public long joinUser(LoginUser login, String name, Provider provider) {
        userValidator.validEmail(login.email());

        return userAppender.append(login, name, provider);
    }

    public FindUser findUser(String email) {
        return userFinder.findByEmail(email);
    }

    @Transactional
    public void loginCallback(Long siteId, String remoteIp, String email, boolean success, String failContent) {
        FindUser findUser = this.findUser(email);

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
        FindUser findUser = userFinder.findByEmail(email);
        return roles.contains(findUser.getUserRole().getCode());
    }

    public FindUser loadUserBySocial(String email, String name, Provider provider) {
        UserTarget.UserTargetBuilder targetBuilder = UserTarget.builder();

        userValidator.validEmailAndProvider(new UserEmailWithProvider(email, provider));

        userFinder.findProviderUser(new UserEmailWithProvider(email, provider)).ifPresentOrElse(user -> {
            targetBuilder.id(user.getId());
        }, () -> {
            long appendedId = userAppender.append(new LoginUser(email, null), name, provider);
            targetBuilder.id(appendedId);
        });

        return userFinder.findUser(targetBuilder.build());
    }
}
