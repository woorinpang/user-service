package io.woorinpang.userservice.storage.db.core.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AdminProvider {
    WOORINPANG("WOORINPANG", "우린팡"),
    GOOGLE("GOOGLE", "구글"),
    NAVER("NAVER", "네이버"),
    KAKAO("KAKAO", "카카오");

    private final String code;
    private final String description;

    public static AdminProvider findByCode(String code) {
        return Arrays.stream(AdminProvider.values())
                .filter(provider -> provider.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("UserState code not found"));
    }

    public static boolean verify(String code) {
        return AdminProvider.GOOGLE.getCode().equals(code) || AdminProvider.NAVER.getCode().equals(code) || AdminProvider.KAKAO.getCode().equals(code);
    }
}
