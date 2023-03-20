package com.woorinpang.userservice.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "woorinpang.user.com", uriPort = 9000)
@ActiveProfiles(profiles = "test")
@Transactional
public class IntegrationTest {

    @PersistenceContext protected EntityManager em;
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;

}
