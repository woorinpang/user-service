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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("UserController 통합테스트")
class UserControllerTest extends IntegrationTest {

    @Nested
    @DisplayName("사용자_회원가입하면_")
    class UserJoin {
        @Test
        @DisplayName("성공하고 상태코드 201과 joinedUserId 를 반환한다.")
        void documentTest() throws Exception {
            //given
            UserJoinRequest request = getUserJoinRequest();

            //expected
            mockMvc.perform(post(API_V1_USER_POST_JOIN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(HttpStatus.CREATED.getReasonPhrase()))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.data.userId").isNumber())
                    /*.andDo(document("user-join",
                            requestFields(
                                    fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                    fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("code").type(JsonFieldType.STRING).description("코드"),
                                    fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("회원가입된 유저 고유 번호")
                            )
                    ))*/
            ;
        }
    }

    @Nested
    @DisplayName("사용자_정보조회하면_")
    class UserInfo {
        @Test
        @DisplayName("성공하고 상태코드 200과 InfoResponse 를 반환한다.")
        void documentTest() throws Exception {
            //given
            User user = getUser();
            em.persist(user);

            //expected
            mockMvc.perform(get(API_V1_USER_GET_INFO, user.getId()))
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
                    /*.andDo(document("user-info",
                            pathParameters(
                                    parameterWithName("userId").description("사용자 고유번호")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("code").type(JsonFieldType.STRING).description("코드"),
                                    fieldWithPath("data.userId").type(JsonFieldType.STRING).description("사용자 고유번호"),
                                    fieldWithPath("data.username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                    fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                    fieldWithPath("data.name").type(JsonFieldType.STRING).description("사용자 이름"),
                                    fieldWithPath("data.roleCode").type(JsonFieldType.STRING).description("권한 코드"),
                                    fieldWithPath("data.userStateCode").type(JsonFieldType.STRING).description("사용자 상태 코드")
                            )
                    ))*/
            ;
        }
    }

    @Nested
    @DisplayName("사용자_정보수정하면_")
    class UserUpdateInfo {
        @Test
        @DisplayName("성공하고 상태코드 200을 반환한다.")
        void documentTest() throws Exception {
            //given
            User user = getUser();
            em.persist(user);

            UserUpdateInfoRequest request = getUserUpdateInfoRequest();

            //expected
            mockMvc.perform(put(API_V1_USER_PUT_UPDATE, user.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.data").isEmpty())
                    /*.andDo(document("user-update-info",
                            pathParameters(
                                    parameterWithName("userId").description("사용자 고유번호")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("code").type(JsonFieldType.STRING).description("코드"),
                                    fieldWithPath("data").ignored()
                            )
                    ))*/
            ;
        }
    }

    @Nested
    @DisplayName("내_비밀번호확인하면_")
    class matchPassword {
        @Test
        @DisplayName("일치하고 상태코드 200과 true를 반환한다.")
        @WithMockCustomUser(username = USERNAME, password = PASSWORD)
        void documentTest() throws Exception {
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
                    /*.andDo(document("match-password",
                            requestFields(
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("code").type(JsonFieldType.STRING).description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("비밀번호 일치 여부")
                            )
                    ))*/
            ;
        }
    }

    @Nested
    @DisplayName("아이디_중복확인하면_")
    class ExistsUsername {
        @Test
        @DisplayName("중복되고 상태코드 200과 true를 반환한다.")
        void documentTest() throws Exception {
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
                    /*.andDo(document("exists-username",
                            requestFields(
                                    fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 아이디")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("code").type(JsonFieldType.STRING).description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("비밀번호 일치 여부")
                            )
                    ))*/
            ;
        }
    }

    @Nested
    @DisplayName("사용자_회원탈퇴하면_")
    class Leave {
        @Test
        @DisplayName("탈퇴처리하고 상태코드 200과 true를 반환한다.")
        @WithMockCustomUser(username = USERNAME, password = PASSWORD)
        void documentTest() throws Exception {
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
                    /*.andDo(document("exists-username",
                            requestFields(
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 아이디"),
                                    fieldWithPath("provider").type(JsonFieldType.STRING).description("로그인 제공자 코드"),
                                    fieldWithPath("token").type(JsonFieldType.STRING).description("사용자 토큰")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("code").type(JsonFieldType.STRING).description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("탈퇴 성공 여부")
                            )
                    ))*/
            ;
        }
    }
}