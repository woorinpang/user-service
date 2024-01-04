package io.woorinpang.userservice.core.domain.user.service.dto;

import io.woorinpang.userservice.core.db.user.UserRole;
import io.woorinpang.userservice.core.db.user.UserState;
import io.woorinpang.userservice.core.db.user.dto.UserSearchCondition;
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
                .searchRole(UserRole.findByName(this.userRole))
                .searchUserState(UserState.findByName(this.userState))
                .build();
    }
}
