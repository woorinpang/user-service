package com.woorinpang.userservice.domain.user;

import com.woorinpang.userservice.domain.user.application.dto.command.UserJoinCommand;
import com.woorinpang.userservice.domain.user.application.dto.command.UserUpdateInfoCommand;
import com.woorinpang.userservice.domain.user.infrastructure.dto.UserSearchCondition;
import com.woorinpang.userservice.domain.user.application.dto.command.SaveUserCommand;
import com.woorinpang.userservice.domain.user.application.dto.command.UpdateUserCommand;
import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
import com.woorinpang.userservice.domain.user.infrastructure.dto.FindPageUserDto;
import com.woorinpang.userservice.domain.user.presentation.admin.request.SaveUserRequest;
import com.woorinpang.userservice.domain.user.presentation.admin.request.UpdateUserRequest;
import com.woorinpang.userservice.domain.user.presentation.user.request.UserJoinRequest;
import com.woorinpang.userservice.domain.user.presentation.user.request.UserUpdateInfoRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserSetup {

    //CREATE_USER
    public static final Long USER_ID = 1L;
    public static final String USERNAME = "chisu";
    public static final String PASSWORD = "Qlalfqjsgh12!@";
    public static final String EMAIL = "chisu@woorinpang.com";
    public static final String NAME = "채치수";
    public static final Role ROLE = Role.USER;
    public static final UserState USER_STATE = UserState.NORMAL;

    //UPDATE_USER
    public static final String UPDATE_EMAIL = "chisuchae@woorinpang.com";
    public static final String UPDATE_NAME = "채치수짱";
    public static final Role UPDATE_ROLE = Role.ADMIN;
    public static final UserState UPDATE_USER_STATE = UserState.NORMAL;

    //ERROR_MESSAGE
    public static final Long USER_NOT_FOUND_ID = 0L;
    public static final String USER_NOT_FOUND_MESSAGE = "UserId=" + USER_NOT_FOUND_ID + "은 존재하지 않습니다.";
    public static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Username=%s은 이미 존재합니다.";
    public static final String EMAIL_ALREADY_EXISTS_MESSAGE = "Email=%s은 이미 존재합니다.";

    //ADMIN_CONTROLLER_REQUEST_URI
    public static final String API_V1_ADMIN_GET_FIND_USERS = "/api/v1/admin/users";
    public static final String API_V1_ADMIN_GET_FIND_USER = "/api/v1/admin/users/{userId}";
    public static final String API_V1_ADMIN_POST_SAVE_USER = "/api/v1/admin/users";
    public static final String API_V1_ADMIN_PUT_UPDATE_USER = "/api/v1/admin/users/{userId}";
    public static final String API_V1_ADMIN_DELETE_DELETE_USER = "/api/v1/admin/users/{userId}";

    //USER_CONTROLLER_REQUEST_URI
    public static final String API_V1_USER_POST_JOIN = "/api/v1/users/join";
    public static final String API_V1_USER_GET_INFO = "/api/v1/users/{userId}";
    public static final String API_V1_USER_PUT_UPDATE = "/api/v1/users/{userId}";

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

    public static LinkedMultiValueMap<String, String> getParams() {
        UserSearchCondition condition = UserSearchCondition.builder()
                .searchKeywordType(UserSearchCondition.KeywordType.NAME)
                .searchKeyword(NAME)
                .searchRole(Role.ADMIN)
                .searchUserState(UserState.NORMAL)
                .build();

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("searchKeywordType", condition.getSearchKeywordType().name());
        params.add("searchKeyword", condition.getSearchKeyword());
        params.add("searchRole", condition.getSearchRole().name());
        params.add("searchUserState", condition.getSearchUserState().name());
        params.add("page", String.valueOf(pageRequest.getOffset()));
        params.add("size", String.valueOf(pageRequest.getPageSize()));
        return params;
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

    public static List<FindPageUserDto> getFindPageUserDtos() {
        return getUsers().stream()
                .map(user -> new FindPageUserDto(user))
                .collect(Collectors.toList());
    }

    public static SaveUserRequest getSaveUserRequest() {
        return SaveUserRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .name(NAME)
                .roleCode(ROLE.getCode())
                .userStateCode(USER_STATE.getCode())
                .build();
    }

    public static UpdateUserRequest getUpdateUserRequest() {
        return UpdateUserRequest.builder()
                .password(PASSWORD)
                .email(UPDATE_EMAIL)
                .name(UPDATE_NAME)
                .roleCode(UPDATE_ROLE.getCode())
                .userStateCode(UPDATE_USER_STATE.getCode())
                .build();
    }

    public static UserJoinRequest getUserJoinRequest() {
        return UserJoinRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .name(NAME)
                .build();
    }

    public static UserUpdateInfoRequest getUserUpdateInfoRequest() {
        return UserUpdateInfoRequest.builder()
                .name(UPDATE_NAME)
                .email(UPDATE_EMAIL)
                .build();
    }

    public static SaveUserCommand getSaveUserCommand() {
        return SaveUserCommand.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .name(NAME)
                .role(ROLE)
                .userState(USER_STATE)
                .build();
    }

    public static UpdateUserCommand getUpdateUserCommand() {
        return UpdateUserCommand.builder()
                .password(PASSWORD)
                .email(UPDATE_EMAIL)
                .name(UPDATE_NAME)
                .role(UPDATE_ROLE)
                .userState(UPDATE_USER_STATE)
                .build();
    }

    public static UserJoinCommand getJoinUserCommand() {
        return UserJoinCommand.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .name(NAME)
                .role(ROLE)
                .userState(USER_STATE)
                .build();
    }

    public static UserUpdateInfoCommand getUserUpdateInfoCommand() {
        return UserUpdateInfoCommand.builder()
                .name(UPDATE_NAME)
                .email(UPDATE_EMAIL)
                .build();
    }
}
