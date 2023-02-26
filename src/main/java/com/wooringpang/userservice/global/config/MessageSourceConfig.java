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
        if ("default".equals(profile)) {
            Path fileStorageLocation = Paths.get(messageDirectory).toAbsolutePath().normalize();
            String dbMessages = StringUtils.cleanPath("file://" + fileStorageLocation);
            messageSource.setBasenames(dbMessages);
        } else {
            messageSource.setBasenames(messageDirectory );
        }
        messageSource.getBasenameSet().forEach(s -> log.info("messageSource getBasenameSet = {}", s));

        messageSource.setCacheSeconds(60); //메시지 파일 변경 감지 간격
        messageSource.setUseCodeAsDefaultMessage(true); //메시지가 없으면 코드를 메시지로 한다.
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }
}
