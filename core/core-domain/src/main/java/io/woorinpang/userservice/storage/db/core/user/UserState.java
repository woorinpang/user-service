package io.woorinpang.userservice.storage.db.core.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserState {
    WAIT("WAIT", "대기"),
    NORMAL("NORMAL", "정상"),
    HALT("HALT", "정지"),
    LEAVE("LEAVE", "탈퇴"),
    DELETE("DELETE", "삭제");

    private final String code;
    private final String description;

    public static UserState findByCode(String code) {
        return Arrays.stream(UserState.values())
                .filter(userState -> userState.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("UserState code not found"));
    }
}
