package com.woorinpang.userservice.core.user.dto;

import com.woorinpang.userservice.core.user.domain.Role;
import com.woorinpang.userservice.core.user.domain.UserState;
import com.woorinpang.userservice.global.dto.CommonSearchCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSearchCondition extends CommonSearchCondition {

    private Role searchRole;
    private UserState searchUserState;
}
