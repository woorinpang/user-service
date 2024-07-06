package io.woorinpang.userservice.storage.db.core.user;

import io.woorinpang.userservice.core.domain.support.error.CoreDomainException;
import io.woorinpang.userservice.core.domain.support.error.DomainErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Provider {
    WOORINPANG("WOORINPANG", "우린팡"),
    GOOGLE("GOOGLE", "구글"),
    NAVER("NAVER", "네이버"),
    KAKAO("KAKAO", "카카오");

    private final String code;
    private final String description;

    public static Provider findByCode(String code) {
        return Arrays.stream(Provider.values())
                .filter(provider -> provider.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new CoreDomainException(DomainErrorType.DEFAULT_ERROR));
    }

    public static boolean verify(String code) {
        return Provider.GOOGLE.getCode().equals(code) || Provider.NAVER.getCode().equals(code) || Provider.KAKAO.getCode().equals(code);
    }
}
