package com.example.PieceOfPeace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** URL 패턴으로 요청이 오면
        // file:///./uploads/ 경로에서 파일을 찾아 제공합니다.
        // 'file:' 접두사는 로컬 파일 시스템 경로를 의미합니다.
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
