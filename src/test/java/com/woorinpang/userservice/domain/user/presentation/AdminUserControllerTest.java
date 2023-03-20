package com.woorinpang.userservice.domain.user.presentation;

import com.woorinpang.userservice.domain.user.application.UserService;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.test.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static com.woorinpang.userservice.domain.user.UserSetup.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
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
        @WithMockUser
        void test01() throws Exception {
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

                    ))
            ;

        }

    }

    @Test
    void findPageUsers() {

    }

    @Test
    void findUser() {
    }

    @Test
    void saveUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}