package com.woorinpang.userservice.domain.user.presentation;

import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.domain.user.presentation.admin.request.SaveUserRequest;
import com.woorinpang.userservice.domain.user.presentation.admin.request.UpdateUserRequest;
import com.woorinpang.userservice.test.IntegrationTest;
import com.woorinpang.userservice.test.WithMockCustomUser;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.woorinpang.userservice.domain.user.UserSetup.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AdminUserController 통합테스트")
class AdminUserControllerTest extends IntegrationTest {

    @Autowired private UserRepository userRepository;
    @Autowired private WebApplicationContext context;

    @Nested
    @DisplayName("사용자_목록_조회하면_")
    class FindUsers {
        @Test
        @DisplayName("성공하고 상태코드 200과 페이지정보 및 데이터를 반환한다.")
        @WithMockUser
        void documentTest() throws Exception {
            //given
            List<User> users = getUsers();
            userRepository.saveAll(users);

            LinkedMultiValueMap<String, String> params = getParams();

            //when
            ResultActions resultActions = mockMvc.perform(get(API_V1_GET_FIND_USERS)
                    .queryParams(params));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.data.content.length()", Matchers.is(5)))
                    .andDo(print())
                    .andDo(document("admin-find-users",
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
                    ));
        }
    }

    @Nested
    @DisplayName("사용자_단건_조회하면_")
    class FindUser {
        @Test
        @DisplayName("성공하고 상태코드 200과 값을 반환한다.")
        @WithMockUser
        void documentTest() throws Exception {
            //given
            User user = getUser();
            em.persist(user);

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
                    .andDo(MockMvcRestDocumentation.document("admin-find-user",
                            pathParameters(
                                    parameterWithName("userId").description("사용자 고유 번호")
                            ),
                            responseFields(
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
                    ))
            ;
        }

        @Test
        @DisplayName("userId = 0L 로 조회 실패하여 UserNotFoundException 이 발생한다.")
        @WithMockUser
        void test01() throws Exception {
            //given

            //expected
            getResultActions(USER_NOT_FOUND_ID)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(USER_NOT_FOUND_MESSAGE))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andDo(print());
        }

        private ResultActions getResultActions(Long userId) throws Exception {
            return mockMvc.perform(get(API_V1_GET_FIND_USER, userId));
        }
    }

    @Nested
    @DisplayName("사용자_저장하면_")
    class SaveUser {

        @BeforeEach
        void init() {
            mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .apply(springSecurity())
                    .build();
        }
        @Test
        @DisplayName("성공하고 상태코드 201과 savedUserId를 반환한다.")
        @WithMockCustomUser
        void documentTest() throws Exception {
            //given
            SaveUserRequest request = getSaveUserRequest();

            //expected
            ResultActions resultActions = mockMvc.perform(post("/api/v1/admin/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            resultActions
                    .andExpect(status().isCreated())
                    .andDo(print());
        }

        @Test
        @DisplayName("")
        @WithMockUser
        void test01() {

        }

    }

    @Nested
    @DisplayName("사용자_수정하면_")
    class UpdateUser {

        @Test
        @DisplayName("성공하고 상태코드 200과 updatedUserId를 반환한다.")
        @WithMockUser
        void updateUser() throws Exception {
            //given
            User user = getUser();
            em.persist(user);

            UpdateUserRequest request = getUpdateUserRequest();

            //when
            ResultActions resultActions = mockMvc.perform(put("/api/v1/admin/users", user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzcHJpbmciLCJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4iLCJ1c2VySWQiOjEsImV4cCI6MTY3OTMyOTkxOX0.Bi1Ta1tlDzuwWp0Q0MH57Rp1NvGx-fBbq6xnmzAyA0e_EVrIGHG38IlT7ffAp0l9w6VDOuIA70rICq2vxIg2SA")
                    .content(objectMapper.writeValueAsString(request)));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andDo(print());
        }

    }

    @Test
    void deleteUser() {
    }
}