package com.woorinpang.userservice.domain.user.presentation;

import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.presentation.user.request.*;
import com.woorinpang.userservice.global.common.entity.Provider;
import com.woorinpang.userservice.test.IntegrationTest;
import com.woorinpang.userservice.test.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;

import static com.woorinpang.userservice.domain.user.UserSetup.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[통합 테스트] UserController")
class UserControllerTest extends IntegrationTest {
    private final String IDENTIFIER = "admin-user-controller-test/%s";

    @Test
    @DisplayName("사용자_회원가입하면_상태코드 201과 joinedUserId 를 반환한다.")
    void userJoin() throws Exception {
        //given
        UserJoinRequest request = getUserJoinRequest();

        //expected
        this.mockMvc.perform(post(API_V1_USER_POST_JOIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(HttpStatus.CREATED.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.userId").isNumber())
                .andDo(document(IDENTIFIER.formatted("user-join"),
                        requestFields(
                                fieldWithPath("username").type(STRING).description("사용자 아이디"),
                                fieldWithPath("password").type(STRING).description("사용자 비밀번호"),
                                fieldWithPath("email").type(STRING).description("사용자 이메일"),
                                fieldWithPath("name").type(STRING).description("사용자 이름"),
                                fieldWithPath("provider").optional().type(STRING).description("제공자"),
                                fieldWithPath("token").optional().type(STRING).description("토큰")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("code").type(STRING).description("코드"),
                                fieldWithPath("data.userId").type(NUMBER).description("회원가입된 유저 고유 번호")
                        )
                ));
    }

    @Test
    @DisplayName("사용자_정보조회하면_상태코드 200과 InfoResponse 를 반환한다.")
    void userInfo() throws Exception {
        //given
        User user = getUser();
        em.persist(user);

        //expected
        this.mockMvc.perform(get(API_V1_USER_GET_INFO, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.userId").value(user.getId()))
                .andExpect(jsonPath("$.data.username").value(USERNAME))
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.name").value(NAME))
                .andExpect(jsonPath("$.data.roleCode").value(ROLE.getCode()))
                .andExpect(jsonPath("$.data.userStateCode").value(USER_STATE.getCode()))
                .andDo(document(IDENTIFIER.formatted("user-info"),
                        pathParameters(
                                parameterWithName("userId").description("사용자 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("code").type(STRING).description("코드"),
                                fieldWithPath("data.userId").type(NUMBER).description("사용자 고유번호"),
                                fieldWithPath("data.username").type(STRING).description("사용자 아이디"),
                                fieldWithPath("data.email").type(STRING).description("사용자 이메일"),
                                fieldWithPath("data.name").type(STRING).description("사용자 이름"),
                                fieldWithPath("data.roleCode").type(STRING).description("역할 코드"),
                                fieldWithPath("data.userStateCode").type(STRING).description("사용자 상태 코드")
                        )
                ));
    }

    @Test
    @DisplayName("사용자_정보수정하면_성공하고 상태코드 200을 반환한다.")
    void userUpdateInfo() throws Exception {
        //given
        User user = getUser();
        em.persist(user);

        UserUpdateInfoRequest request = getUserUpdateInfoRequest();

        //expected
        this.mockMvc.perform(put(API_V1_USER_PUT_UPDATE, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(document(IDENTIFIER.formatted("user-update-info"),
                        pathParameters(
                                parameterWithName("userId").description("사용자 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("code").type(STRING).description("코드"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }

    @Test
    @DisplayName("내_비밀번호확인하면_일치하고 상태코드 200과 true를 반환한다.")
    @WithMockCustomUser(username = USERNAME, password = PASSWORD)
    void matchPassword() throws Exception {
        //given
        User user = getUser();
        em.persist(user);

        UserMatchPasswordRequest request = getUserMatchPasswordRequest();

        //expected
        mockMvc.perform(post(API_V1_USER_MATCH_PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").value(Boolean.TRUE))
                .andDo(document(IDENTIFIER.formatted("match-password"),
                        requestFields(
                                fieldWithPath("password").type(STRING).description("사용자 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("code").type(STRING).description("코드"),
                                fieldWithPath("data").type(BOOLEAN).description("비밀번호 일치 여부")
                        )
                ))
        ;
    }
    @Test
    @DisplayName("아이디_중복확인하면_중복되고 상태코드 200과 true를 반환한다.")
    void existsUsername() throws Exception {
        //given
        User user = getUser();
        em.persist(user);

        UserExistsUsernameRequest request = getUserExistsUsernameRequest();

        //expected
        mockMvc.perform(post(API_V1_USER_EXISTS_USERNAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").value(Boolean.TRUE))
                .andDo(document(IDENTIFIER.formatted("exists-username"),
                        requestFields(
                                fieldWithPath("username").type(STRING).description("사용자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("code").type(STRING).description("코드"),
                                fieldWithPath("data").type(BOOLEAN).description("비밀번호 일치 여부")
                        )
                ));
    }

    @Test
    @DisplayName("사용자_회원탈퇴하면_탈퇴처리하고 상태코드 200과 true를 반환한다.")
    @WithMockCustomUser(username = USERNAME, password = PASSWORD)
    void leave() throws Exception {
        //given
        User user = getUser();
        em.persist(user);

        UserLeaveRequest request = getUserLeaveRequest(PASSWORD, Provider.WOORINPANG.getCode(), "");

        //expected
        mockMvc.perform(post(API_V1_USER_LEAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").value(Boolean.TRUE))
                .andDo(document("exists-username",
                        requestFields(
                                fieldWithPath("password").type(STRING).description("사용자 아이디"),
                                fieldWithPath("provider").type(STRING).description("로그인 제공자 코드"),
                                fieldWithPath("token").type(STRING).description("사용자 토큰")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("code").type(STRING).description("코드"),
                                fieldWithPath("data").type(BOOLEAN).description("탈퇴 성공 여부")
                        )
                ));
    }
}