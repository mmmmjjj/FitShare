package com.fitshare.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // TODO: EC2서버 CORS 설정
        // TODO: Spring Security CORS 설정
        // https://kukekyakya.tistory.com/entry/Spring-Boot-CORS-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0
        // 위 주소 내용 참고해서 설정
        registry.addMapping("/**")
                .allowedOrigins("*");
    }
}
