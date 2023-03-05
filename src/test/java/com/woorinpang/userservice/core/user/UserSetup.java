package com.woorinpang.userservice.core.user;

import com.woorinpang.common.entity.Role;
import com.woorinpang.userservice.core.user.domain.User;
import com.woorinpang.userservice.core.user.domain.UserState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class UserSetup {

    //USER
    public static final Long USER_ID = 1L;
    public static final String USERNAME = "chisu";
    public static final String EMAIL = "chisu@woorinpang.com";
    public static final String NAME = "채치수";
    public static final String PASSWORD = "1234";

    public static User setUser() {
        return User.createBuilder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .role(Role.USER)
                .userState(UserState.NORMAL)
                .build();
    }

    public static List<User> setUsers() {
        List<User> users = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            User user = i % 2 == 0 ?
                    User.createBuilder()
                            .username(USERNAME + i)
                            .email(EMAIL + i)
                            .password(PASSWORD)
                            .name(NAME + "A")
                            .role(Role.USER)
                            .userState(UserState.NORMAL)
                            .build()
                    :
                    User.createBuilder()
                            .username(USERNAME + i)
                            .email(EMAIL + i)
                            .password(PASSWORD)
                            .name(NAME + "B")
                            .role(Role.USER)
                            .userState(UserState.NORMAL)
                            .build();
            users.add(user);
        });
        return users;
    }
}
