package com.woorinpang.userservice.test.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

@TestConfiguration
public class IntegrationTestConfig {

    @Bean
    public RestDocumentationResultHandler restDocsMockMvcConfigurationCustomizer() {
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}", //identifier
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        );
    }
}
