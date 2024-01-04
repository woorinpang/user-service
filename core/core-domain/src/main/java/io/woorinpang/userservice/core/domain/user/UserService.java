package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserFinder userFinder;
    private final UserAppender userAppender;
    private final UserModifier userModifier;
    private final UserRemover userRemover;
    private final UserValidator userValidator;

    /**
     * 사용자 회원가입
     */
    public long userJoin(UserLogin login, UserInfo info) {
        userValidator.duplicateLoginId(login.id());
        userValidator.duplicateEmail(info.getEmail());

        return userAppender.append(login, info);
    }

    /**
     * 사용자 정보 조회
     */
    public FindUser findUserInfo(long userId) {
        return userFinder.findUser(userId);
    }

    /**
     * 사용자 정보 수정
     */
    public void modifyUserInfo(UserTarget target, UserInfo info) {
        userValidator.duplicateEmail(info.getEmail());

        userModifier.modify(target, info);
    }

    /**
     * 사용자 아이디 중복확인
     */
    public boolean existsUsername(String username) {
        return userFinder.findUserByUsername(username).isPresent();
    }

    /**
     * 사용자 회원탈퇴
     */
    public void userLeave(UserTarget userTarget) {
        userRemover.leave(userTarget.id());

    }

}
