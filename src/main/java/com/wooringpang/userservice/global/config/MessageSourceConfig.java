package com.wooringpang.userservice.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Configuration
public class MessageSourceConfig {

    @Value("${messages.directory}")
    private String messageDirectory;

    @Value("${spring.profiles.active:default}")
    private String profile;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        System.out.println("messageDirectory = " + messageDirectory);
        final String MESSAGES = "/messages";
        final String ERRORS = "/errors";
        messageSource.addBasenames("classpath:" + MESSAGES);
        messageSource.addBasenames("classpath:" + ERRORS);
//        if ("default".equals(profile)) {
//            Path fileStorageLocation = Paths.get(messageDirectory).toAbsolutePath().normalize();
//            String dbMessages = StringUtils.cleanPath("file://" + fileStorageLocation + MESSAGES);
//            messageSource.addBasenames(dbMessages);
//        } else {
//            messageSource.addBasenames(messageDirectory + MESSAGES);
//        }
        messageSource.addBasenames("file:///Users/heechul/workspace/github/woorinpang/attach/messages/messages");
        messageSource.getBasenameSet().forEach(s -> log.info("messageSource getBasenameSet = {}", s));

        messageSource.setCacheSeconds(60); //메시지 파일 변경 감지 간격
        messageSource.setUseCodeAsDefaultMessage(true); //메시지가 없으면 코드를 메시지로 한다.
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }
}
