package com.wooringpang.userservice.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStateCode {

    WAIT("00", "대기"),
    NORMAL("01", "정상"),
    HALT("07", "정지"),
    LEAVE("09", "탈퇴"),
    DELETE("09", "삭제");

    private final String code;
    private final String description;
}
