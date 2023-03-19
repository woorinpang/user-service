package com.woorinpang.userservice.domain.user;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class UserSetup {

    //CREATE_USER
    public static final Long USER_ID = 1L;
    public static final String USERNAME = "chisu";
    public static final String EMAIL = "chisu@woorinpang.com";
    public static final String NAME = "채치수";
    public static final String PASSWORD = "Qlalfqjsgh12!@";

    public static User getUser() {
        return User.createBuilder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .name(NAME)
                .role(Role.USER)
                .userState(UserState.NORMAL)
                .build();
    }

    public static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            User user = i % 2 == 0 ?
                    User.createBuilder()
                            .username(USERNAME + i)
                            .email(EMAIL + i)
                            .password(PASSWORD)
                            .name(NAME + "A")
                            .role(Role.ADMIN)
                            .userState(UserState.NORMAL)
                            .build()
                    :
                    User.createBuilder()
                            .username(USERNAME + i)
                            .email(EMAIL + i)
                            .password(PASSWORD)
                            .name(NAME + "B")
                            .role(Role.USER)
                            .userState(UserState.LEAVE)
                            .build();
            users.add(user);
        });
        return users;
    }
}
