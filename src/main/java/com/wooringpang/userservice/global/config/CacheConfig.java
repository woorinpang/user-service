package com.wooringpang.userservice.global.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableCaching
@EnableAspectJAutoProxy(exposeProxy = true) //AopContext.currentProxy() 사용 옵가
public class CacheConfig {
}
