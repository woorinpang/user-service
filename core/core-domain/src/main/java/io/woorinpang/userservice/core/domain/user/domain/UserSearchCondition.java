package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.enums.user.UserRole;
import io.woorinpang.userservice.core.enums.user.UserState;
import lombok.*;

import java.util.Arrays;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSearchCondition {

    private String searchKeyword;
    private KeywordType searchKeywordType;
    private UserRole searchRole;
    private UserState searchUserState;

    @Builder
    public UserSearchCondition(String searchKeyword, KeywordType searchKeywordType, UserRole searchRole, UserState searchUserState) {
        this.searchKeyword = searchKeyword;
        this.searchKeywordType = searchKeywordType;
        this.searchRole = searchRole;
        this.searchUserState = searchUserState;
    }

    @Getter
    @AllArgsConstructor
    public enum KeywordType {
        NAME("name", "사용자 이름"),
        EMAIL("email", "사용자 이메일"),
        ;

        private final String code;
        private final String description;

        public static KeywordType findByName(String name) {
            return Arrays.stream(KeywordType.values())
                    .filter(keywordType -> keywordType.name().equals(name))
                    .findFirst()
                    .orElse(null);
        }
    }
}
