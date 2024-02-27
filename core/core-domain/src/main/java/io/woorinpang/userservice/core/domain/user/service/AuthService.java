package io.woorinpang.userservice.core.domain.user.service;

import io.woorinpang.userservice.core.domain.user.*;
import io.woorinpang.userservice.storage.core.db.user.dto.UserLoginLogCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserFinder userFinder;
    private final UserAppender userAppender;
    private final UserModifier userModifier;
    private final UserValidator userValidator;
    private final UserLogger userLogger;

    /**
     * 사용자 아이디 중복확인
     */
    public boolean existsUsername(String username) {
        return userFinder.findByUsername(username).isPresent();
    }

    /**
     * 사용자 회원가입
     */
    public long userJoin(UserLogin login, UserInfo info) {
        userValidator.duplicateLoginId(login.id());

        return userAppender.append(login, info);
    }

    public Optional<FindUser> findByUsername(String username) {
        return userFinder.findByUsername(username)
                .map(user -> new FindUser(user));
    }

    @Transactional
    public void loginCallback(Long siteId, String remoteIp, String username,  boolean success, String failContent) {
        FindUser findUser = this.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found in the database!!"));

        if (Boolean.TRUE.equals(success)) {
            userModifier.successLogin(new UserTarget(findUser.getId()));
        } else {
            userModifier.failLogin(new UserTarget(findUser.getId()));
        }

        // 로그인 로그 입력
        UserLoginLogCommand command = UserLoginLogCommand.builder()
                .siteId(siteId)
                .username(username)
                .remoteIp(remoteIp)
                .success(success)
                .failContent(failContent)
                .build();
        userLogger.log(command);
    }

    public boolean isAuthorization(String username, String httpMethod, String requestPath) {
        return userFinder.findByUsername(username).isPresent();
    }

    public void updateRefreshToken(long userId, String refreshToken) {
        userModifier.modifyRefreshToken(new UserTarget(userId), refreshToken);
    }

    public Optional<FindUser> findByRefreshToken(String refreshToken) {
        return userFinder.findByRefreshToken(refreshToken)
                .map(user -> new FindUser(user));
    }
}
