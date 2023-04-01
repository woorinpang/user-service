package com.woorinpang.userservice.global.common.entity;

import com.woorinpang.userservice.global.common.exception.ProviderTypeMismatchException;
import com.woorinpang.userservice.global.exception.EnumTypeMismatchException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Provider {
    GOOGLE("Google", "구글"),
    NAVER("Naver", "네이버"),
    KAKAO("Kakao", "카카오");

    private final String code;
    private final String description;

    public static Provider findByCode(String code) {
        return Arrays.stream(Provider.values())
                .filter(provider -> provider.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new ProviderTypeMismatchException(code));
    }
}
