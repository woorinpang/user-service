package io.woorinpang.userservice.core.domain.user.application;

import io.woorinpang.userservice.core.domain.user.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserFinder userFinder;
    private final UserAppender userAppender;
    private final UserModifier userModifier;
    private final UserRemover userRemover;
    private final UserValidator userValidator;

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
        userModifier.modify(target, info);
    }

    /**
     * 사용자 회원탈퇴
     */
    public void userLeave(UserTarget userTarget) {
        userRemover.leave(userTarget.id());
    }
}
