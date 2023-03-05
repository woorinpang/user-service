package com.woorinpang.userservice.core.user.dto;

import com.woorinpang.common.dto.CommonSearchCondition;
import com.woorinpang.common.entity.Role;
import com.woorinpang.userservice.core.user.domain.UserState;
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
