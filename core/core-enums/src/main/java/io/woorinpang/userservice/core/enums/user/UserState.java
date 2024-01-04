package io.woorinpang.userservice.core.enums.user;

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

    public static UserState findByName(String name) {
        return Arrays.stream(UserState.values())
                .filter(userState -> userState.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static UserState findByCode(String code) {
        return Arrays.stream(UserState.values())
                .filter(userState -> userState.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "은 없는 유저상태코드입니다."));
    }
}
