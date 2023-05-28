package com.woorinpang.userservice.domain.user.presentation;

import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.domain.user.presentation.admin.request.SaveUserRequest;
import com.woorinpang.userservice.domain.user.presentation.admin.request.UpdateUserRequest;
import com.woorinpang.userservice.test.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static com.woorinpang.userservice.domain.user.UserSetup.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[통합 테스트] AdminUserController")
class AdminUserControllerTest extends IntegrationTest {
    private final String IDENTIFIER = "admin-user-controller-test/%s";
    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("사용자_목록_조회하면_상태코드 200과 페이지정보 및 데이터를 반환한다.")
    void findPageUser() throws Exception {
        //given
        List<User> users = getUsers();
        userRepository.saveAll(users);

        LinkedMultiValueMap<String, String> params = getParams();

        //expected
        this.mockMvc.perform(get(API_V1_ADMIN_GET_FIND_USERS)
                        .queryParams(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.content.length()", Matchers.is(5)))
                .andDo(print())
                .andDo(document(IDENTIFIER.formatted("find-page-user"),
                        queryParameters(
                                parameterWithName("searchKeywordType").description("검색 조건 유형"),
                                parameterWithName("searchKeyword").description("검색 키워드"),
                                parameterWithName("searchRole").description("검색 권한"),
                                parameterWithName("searchUserState").description("검색 유저상태"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("code").type(STRING).description("상태코드"),
                                fieldWithPath("data.content[*].userId").type(NUMBER).description("사용자 고유번호"),
                                fieldWithPath("data.content[*].username").type(STRING).description("사용자 아이디"),
                                fieldWithPath("data.content[*].email").type(STRING).description("사용자 이메일"),
                                fieldWithPath("data.content[*].name").type(STRING).description("사용자 이름"),
                                fieldWithPath("data.content[*].roleCode").type(STRING).description("역할 코드"),
                                fieldWithPath("data.content[*].roleDescription").type(STRING).description("역학 설명"),
                                fieldWithPath("data.content[*].userStateCode").type(STRING).description("사용자 상태 코드"),
                                fieldWithPath("data.content[*].userStateDescription").type(STRING).description("사용자 상태 설명"),
                                fieldWithPath("data.content[*].lastLoginDate").optional().type(STRING).description("마지막 로그인 일자"),
                                fieldWithPath("data.content[*].loginFailCount").type(NUMBER).description("로그인 실패 횟수"),
                                subsectionWithPath("data.pageable").type(OBJECT).description("pageable"),
                                subsectionWithPath("data.totalPages").type(NUMBER).description("totalPages"),
                                subsectionWithPath("data.totalElements").type(NUMBER).description("totalElements"),
                                subsectionWithPath("data.last").type(BOOLEAN).description("last"),
                                subsectionWithPath("data.size").type(NUMBER).description("size"),
                                subsectionWithPath("data.number").type(NUMBER).description("number"),
                                subsectionWithPath("data.sort").type(OBJECT).description("sort"),
                                subsectionWithPath("data.numberOfElements").type(NUMBER).description("numberOfElements"),
                                subsectionWithPath("data.first").type(BOOLEAN).description("first"),
                                subsectionWithPath("data.empty").type(BOOLEAN).description("empty")
                        )
                ));
    }

    @Test
    @DisplayName("사용자_단건_조회하면_상태코드 200과 사용자 정보를 반환한다.")
    void findUser() throws Exception {
        //given
        User user = getUser();
        em.persist(user);

        //expected
        this.mockMvc.perform(get(API_V1_ADMIN_GET_FIND_USER, user.getId()))
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
                .andDo(document(IDENTIFIER.formatted("find-user"),
                        pathParameters(
                                parameterWithName("userId").description("사용자 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("code").type(STRING).description("응답코드"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("data.userId").type(NUMBER).description("사용자 고유번호"),
                                fieldWithPath("data.username").type(STRING).description("사용자 아이디"),
                                fieldWithPath("data.email").type(STRING).description("사용자 이메일"),
                                fieldWithPath("data.name").type(STRING).description("사용자 이름"),
                                fieldWithPath("data.roleCode").type(STRING).description("역할 코드"),
                                fieldWithPath("data.userStateCode").type(STRING).description("사용자 상태코드"),
                                fieldWithPath("data.googleId").optional().type(STRING).description("구글 아이디"),
                                fieldWithPath("data.kakaoId").optional().type(STRING).description("카카오 아이디"),
                                fieldWithPath("data.naverId").optional().type(STRING).description("네이버 아이디"),
                                fieldWithPath("data.isSocialUser").optional().type(BOOLEAN).description("소셜 사용자 여부"),
                                fieldWithPath("data.hasPassword").type(BOOLEAN).description("비밀번호 여부")
                        )
                ));
    }

    @Test
    @DisplayName("사용자_저장하면_상태코드 201과 userId를 반환한다.")
    void saveUser() throws Exception {
        //given
        SaveUserRequest request = getSaveUserRequest();

        //expected
        this.mockMvc.perform(post("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(HttpStatus.CREATED.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.data.userId").isNotEmpty())
                .andDo(print())
                .andDo(document(IDENTIFIER.formatted("save-user"),
                        requestFields(
                                fieldWithPath("username").type(STRING).description("사용자 아이디"),
                                fieldWithPath("password").type(STRING).description("사용자비밀번호"),
                                fieldWithPath("email").type(STRING).description("사용자 이메일"),
                                fieldWithPath("name").type(STRING).description("사용자 이름"),
                                fieldWithPath("roleCode").type(STRING).description("권한 코드"),
                                fieldWithPath("userStateCode").type(STRING).description("사용자 상태 코드")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("code").type(STRING).description("응답코드"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("data.userId").type(NUMBER).description("저장된 사용자 고유번호")
                        )
                ));
    }


    @Test
    @DisplayName("사용자_수정하면_상태코드 200과 userId를 반환한다.")
    void updateUser() throws Exception {
        //given
        User user = getUser();
        UpdateUserRequest request = getUpdateUserRequest();
        em.persist(user);

        //expected
        this.mockMvc.perform(put(API_V1_ADMIN_PUT_UPDATE_USER, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.userId").isNotEmpty())
                .andDo(print())
                .andDo(document(IDENTIFIER.formatted("update-user"),
                        requestFields(
                                fieldWithPath("password").type(STRING).description("사용자 패스워드"),
                                fieldWithPath("email").type(STRING).description("사용자 이메일"),
                                fieldWithPath("name").type(STRING).description("사용자 이름"),
                                fieldWithPath("roleCode").type(STRING).description("역할 코드"),
                                fieldWithPath("userStateCode").type(STRING).description("사용자 상태 코드")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("code").type(STRING).description("응답코드"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("data.userId").type(NUMBER).description("수정된 사용자 고유번호")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("사용자_삭제하면_성공하고 상태코드 204를 반환한다.")
    void deleteUser() throws Exception {
        //given
        User user = getUser();
        userRepository.save(user);

        //expected
        this.mockMvc.perform(delete(API_V1_ADMIN_DELETE_DELETE_USER, user.getId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value(HttpStatus.NO_CONTENT.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.NO_CONTENT.value()))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print())
                .andDo(document(IDENTIFIER.formatted("delete-user"),
                        pathParameters(
                                parameterWithName("userId").description("사용자 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").type(STRING).description("api 요청 시간"),
                                fieldWithPath("code").type(STRING).description("응답코드"),
                                fieldWithPath("message").type(STRING).description("메시지"),
                                fieldWithPath("status").type(NUMBER).description("상태코드"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }
}