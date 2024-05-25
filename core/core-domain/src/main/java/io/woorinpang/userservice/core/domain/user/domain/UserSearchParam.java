package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.enums.user.UserRole;
import io.woorinpang.userservice.core.enums.user.UserState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSearchParam {
    private String keyword;
    private String keywordType;
    private String userRole;
    private String userState;

    public UserSearchCondition toCondition() {
        return UserSearchCondition.builder()
                .searchKeyword(this.keyword)
                .searchKeywordType(UserSearchCondition.KeywordType.findByName(this.keywordType))
                .searchRole(UserRole.findByCode(this.userRole))
                .searchUserState(UserState.findByCode(this.userState))
                .build();
    }
}
