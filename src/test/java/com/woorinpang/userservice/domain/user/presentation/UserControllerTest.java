package com.woorinpang.userservice.domain.user.presentation;

import com.woorinpang.userservice.domain.user.presentation.user.request.JoinRequest;
import com.woorinpang.userservice.test.IntegrationTest;
import jdk.security.jarsigner.JarSigner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.woorinpang.userservice.domain.user.UserSetup.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("UserController 통합테스트")
class UserControllerTest extends IntegrationTest {

    @Nested
    @DisplayName("사용자_회원가입하면_")
    class Join {
        @Test
        @DisplayName("성공하고 상태코드 201과 joinedUserId를 반환한다.")
        void documentTest() throws Exception {
            //given
            JoinRequest request = JoinRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .email(EMAIL)
                    .name(NAME)
                    .build();

            //expected
            mockMvc.perform(post("/api/v1/users/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(HttpStatus.CREATED.getReasonPhrase()))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.data.joinedUserId").isNumber())
                    .andDo(print())
                    /*.andDo(document("join",
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
                                    fieldWithPath("data.joinedUserId").type(JsonFieldType.NUMBER).description("회원가입된 유저 고유 번호")
                            )
                    ))*/
            ;
        }
    }
}