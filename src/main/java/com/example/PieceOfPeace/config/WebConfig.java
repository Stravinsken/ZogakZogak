package com.example.PieceOfPeace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                // 특정 출처만 명시적으로 허용합니다.
                .allowedOrigins("http://localhost:3000", "http://localhost:5173", "http://54.180.125.150") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 모든 HTTP 메소드를 허용
                .allowedHeaders("*") // 모든 헤더를 허용
                .allowCredentials(true); // 쿠키/인증 정보 허용
    }
}
