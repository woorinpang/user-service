package com.woorinpang.userservice.domain.user.infrastructure.dto;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.UserState;
import com.woorinpang.userservice.global.common.dto.CommonSearchCondition;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSearchCondition extends CommonSearchCondition {

    private KeywordType searchKeywordType;
    private Role searchRole;
    private UserState searchUserState;

    @Builder
    public UserSearchCondition(String searchKeyword, KeywordType searchKeywordType, Role searchRole, UserState searchUserState) {
        super(searchKeyword);
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
    }
}
