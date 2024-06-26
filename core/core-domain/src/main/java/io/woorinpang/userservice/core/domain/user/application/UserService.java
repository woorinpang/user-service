package io.woorinpang.userservice.core.domain.user.application;

import io.woorinpang.userservice.core.domain.user.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserFinder userFinder;
    private final UserModifier userModifier;
    private final UserRemover userRemover;

    /**
     * 사용자 정보 조회
     */
    public FindUser findUserInfo(UserTarget target) {
        return userFinder.findUser(target);
    }

    /**
     * 사용자 정보 수정
     */
    public void modifyUser(UserTarget target, String name) {
        userModifier.modify(target, name);
    }

    /**
     * 사용자 회원탈퇴
     */
    public void leaveUser(UserTarget target) {
        userRemover.leave(target);
    }
}
