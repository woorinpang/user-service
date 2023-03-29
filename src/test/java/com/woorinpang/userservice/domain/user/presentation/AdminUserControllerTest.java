package com.woorinpang.userservice.domain.user.presentation;

import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.domain.user.presentation.admin.request.SaveUserRequest;
import com.woorinpang.userservice.domain.user.presentation.admin.request.UpdateUserRequest;
import com.woorinpang.userservice.test.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static com.woorinpang.userservice.domain.user.UserSetup.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AdminUserController 통합테스트")
class AdminUserControllerTest extends IntegrationTest {

    @Autowired private UserRepository userRepository;

    @Nested
    @DisplayName("사용자_목록_조회하면_")
    class FindUsers {
        @Test
        @DisplayName("성공하고 상태코드 200과 페이지정보 및 데이터를 반환한다.")
        void documentTest() throws Exception {
            //given
            List<User> users = getUsers();
            userRepository.saveAll(users);

            LinkedMultiValueMap<String, String> params = getParams();

            //when
            ResultActions resultActions = mockMvc.perform(get(API_V1_ADMIN_GET_FIND_USERS)
                    .queryParams(params));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.data.content.length()", Matchers.is(5)))
                    .andDo(print())
                    /*.andDo(document("admin-find-users",
                            queryParameters(
                                    parameterWithName("searchKeywordType").description("검색 조건 유형"),
                                    parameterWithName("searchKeyword").description("검색 키워드"),
                                    parameterWithName("searchRole").description("검색 권한"),
                                    parameterWithName("searchUserState").description("검색 유저상태"),
                                    parameterWithName("page").description("페이지 번호"),
                                    parameterWithName("size").description("페이지 사이즈")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("data.content[*].userId").type(JsonFieldType.STRING).description("사용자 고유 번호"),
                                    fieldWithPath("data.content[*].username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                    fieldWithPath("data.content[*].email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                    fieldWithPath("data.content[*].name").type(JsonFieldType.STRING).description("사용자 이름"),
                                    fieldWithPath("data.content[*].roleCode").type(JsonFieldType.STRING).description("권한 코드"),
                                    fieldWithPath("data.content[*].roleDescription").type(JsonFieldType.STRING).description("권한 설명"),
                                    fieldWithPath("data.content[*].userStateCode").type(JsonFieldType.STRING).description("사용자 상태 코드"),
                                    fieldWithPath("data.content[*].userStateDescription").type(JsonFieldType.STRING).description("사용자 상태 설명"),
                                    fieldWithPath("data.content[*].lastLoginDate").type(JsonFieldType.STRING).description("최신 로그인 시간"),
                                    fieldWithPath("data.content[*].loginFailCount").type(JsonFieldType.STRING).description("로그인 실패 횟수")
                            )
                    ))*/
            ;
        }
    }

    @Nested
    @DisplayName("사용자_단건_조회하면_")
    class FindUser {
        User user = getUser();

        @BeforeEach
        void init() {
            //given
            em.persist(user);
        }

        @Test
        @DisplayName("성공하고 상태코드 200과 값을 반환한다.")
        void documentTest() throws Exception {
            //expected
            getResultActions(user.getId())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.data.userId").value(user.getId()))
                    .andExpect(jsonPath("$.data.username").value(USERNAME))
                    .andExpect(jsonPath("$.data.email").value(EMAIL))
                    .andExpect(jsonPath("$.data.name").value(NAME))
                    .andExpect(jsonPath("$.data.roleCode").value(ROLE.getCode()))
                    .andExpect(jsonPath("$.data.userStateCode").value(USER_STATE.getCode()))
                    .andExpect(jsonPath("$.data.hasPassword").value(Boolean.TRUE))
                    .andDo(print())
                    /*.andDo(MockMvcRestDocumentation.document("admin-find-user",
                            pathParameters(
                                    parameterWithName("userId").description("사용자 고유 번호")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 고유 번호"),
                                    fieldWithPath("username").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                                    fieldWithPath("email").type(JsonFieldType.NUMBER).description("사용자 이메일"),
                                    fieldWithPath("name").type(JsonFieldType.NUMBER).description("사용자 이름"),
                                    fieldWithPath("roleCode").type(JsonFieldType.NUMBER).description("권한 코드"),
                                    fieldWithPath("userStateCode").type(JsonFieldType.NUMBER).description("사용자 상태 코드"),
                                    fieldWithPath("googleId").type(JsonFieldType.NUMBER).description("구글 아이디"),
                                    fieldWithPath("kakaoId").type(JsonFieldType.NUMBER).description("카카오 아이디"),
                                    fieldWithPath("naverId").type(JsonFieldType.NUMBER).description("네이버 아이디"),
                                    fieldWithPath("isSocialUser").type(JsonFieldType.BOOLEAN).description("소셜 사용자 여부"),
                                    fieldWithPath("hasPassword").type(JsonFieldType.BOOLEAN).description("비밀번호 여부")
                            )
                    ))*/
            ;
        }

        @Test
        @DisplayName("userId = 0L 로 조회 실패하여 UserNotFoundException 이 발생한다.")
        void test01() throws Exception {
            //expected
            getResultActions(USER_NOT_FOUND_ID)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(USER_NOT_FOUND_MESSAGE))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andDo(print());
        }

        private ResultActions getResultActions(Long userId) throws Exception {
            return mockMvc.perform(get(API_V1_ADMIN_GET_FIND_USER, userId));
        }
    }

    @Nested
    @DisplayName("사용자_저장하면_")
    class SaveUser {
        @Test
        @DisplayName("성공하고 상태코드 201과 userId를 반환한다.")
        void documentTest() throws Exception {
            //given
            SaveUserRequest request = getSaveUserRequest();

            //expected
            ResultActions resultActions = mockMvc.perform(post("/api/v1/admin/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            resultActions
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(HttpStatus.CREATED.getReasonPhrase()))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andExpect(jsonPath("$.data.userId").isNotEmpty())
                    .andDo(print())
                    /*.andDo(document("admin-save-user",
                            PayloadDocumentation.requestFields(
                                    PayloadDocumentation.fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                    PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("사용자비밀번호"),
                                    PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                    PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                                    PayloadDocumentation.fieldWithPath("roleCode").type(JsonFieldType.STRING).description("권한 코드"),
                                    PayloadDocumentation.fieldWithPath("userStateCode").type(JsonFieldType.STRING).description("사용자 상태 코드")
                            ),
                            PayloadDocumentation.responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("data.userId를").type(JsonFieldType.STRING).description("저장된 사용자 고유 번호")
                            )
                    ))*/
            ;
        }
    }

    @Nested
    @DisplayName("사용자_수정하면_")
    class UpdateUser {
        User user = getUser();
        UpdateUserRequest request = getUpdateUserRequest();

        @BeforeEach
        void init() {
            //given
            em.persist(user);
        }

        @Test
        @DisplayName("성공하고 상태코드 200과 userId를 반환한다.")
        void updateUser() throws Exception {
            //expected
            getResultActions(user.getId(), request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.data.userId").isNotEmpty())
                    .andDo(print())
                    /*.andDo(document("admin-update-user",
                            PayloadDocumentation.requestFields(
                                    PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 패스워드"),
                                    PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                    PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                                    PayloadDocumentation.fieldWithPath("roleCode").type(JsonFieldType.STRING).description("권한 코드"),
                                    PayloadDocumentation.fieldWithPath("userStateCode").type(JsonFieldType.STRING).description("사용자 상태 코드")
                            ),
                            PayloadDocumentation.responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("data.userId").type(JsonFieldType.STRING).description("수정된 사용자 고유 번호")
                            )
                    ))*/
            ;
        }

        @Test
        @DisplayName("userId = 0L 로 조회 실패하여 UserNotFoundException 이 발생한다.")
        void test01() throws Exception {
            //expected
            getResultActions(USER_NOT_FOUND_ID, request)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(USER_NOT_FOUND_MESSAGE))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andDo(print());
        }

        private ResultActions getResultActions(Long userId, UpdateUserRequest request) throws Exception {
            return mockMvc.perform(put(API_V1_ADMIN_PUT_UPDATE_USER, userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }
    }

    @Nested
    @DisplayName("사용자_삭제하면_")
    class DeleteUser {
        User user = getUser();

        @BeforeEach
        void init() {
            //given
            userRepository.save(user);
        }

        @Test
        @DisplayName("성공하고 상태코드 204를 반환한다.")
        void deleteUser() throws Exception {
            //expected
            getResultActions(user.getId())
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$.message").value(HttpStatus.NO_CONTENT.getReasonPhrase()))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NO_CONTENT.value()))
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print())
                    /*.andDo(document("admin-delete-user",
                            pathParameters(
                                    parameterWithName("userId").description("사용자 고유 번호")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("api 요청 시간,"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("상태코드"),
                                    fieldWithPath("data").ignored()
                            )
                    ))*/
            ;
        }

        @Test
        @DisplayName("userId = 0L 로 조회 실패하여 UserNotFoundException 이 발생한다.")
        void test01() throws Exception {
            //expected
            getResultActions(USER_NOT_FOUND_ID)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(USER_NOT_FOUND_MESSAGE))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andDo(print());
        }

        private ResultActions getResultActions(Long userId) throws Exception {
            return mockMvc.perform(delete(API_V1_ADMIN_DELETE_DELETE_USER, userId));
        }
    }
}