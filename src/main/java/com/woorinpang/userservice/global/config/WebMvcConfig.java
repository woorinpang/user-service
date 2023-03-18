package com.woorinpang.userservice.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Profile("!test")
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
}
