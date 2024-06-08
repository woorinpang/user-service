package io.woorinpang.userservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.woorinpang.userservice.core.api.config.TokenProvider;
import io.woorinpang.userservice.core.api.config.dto.LoginUser;
import io.woorinpang.userservice.core.domain.user.domain.FindUser;
import io.woorinpang.userservice.core.domain.user.domain.UserRole;
import io.woorinpang.userservice.core.domain.user.domain.UserState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "local")
class TokenProviderTest {
    @Autowired private TokenProvider tokenProvider;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void createAccessToken() {

        FindUser findUser = new FindUser(1L, "spring@woorinpang.com", "", "스프링", UserRole.ROLE_USER, UserState.NORMAL);
        LoginUser loginUser = new LoginUser(findUser);
        try {
            String payload = new ObjectMapper().writeValueAsString(loginUser);
            String token = tokenProvider.createAccessToken("spring", findUser.getUserRole().getCode(), payload);
            System.out.println("token = " + token);

            Claims claims = tokenProvider.getClaimsFromToken(token);
            String user = (String) claims.get("user");
            LoginUser parseLoginUser = objectMapper.readValue(user, LoginUser.class);

            assertThat(parseLoginUser.getUserId()).isEqualTo(findUser.getId());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}