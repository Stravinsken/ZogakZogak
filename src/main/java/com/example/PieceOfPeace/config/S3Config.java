package com.example.PieceOfPeace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Configuration
    @Profile("!local") // "local" 프로필이 아닐 때만 이 설정을 사용 (운영 환경)
    public static class AwsS3Config {

        @Value("${spring.cloud.aws.credentials.access-key}")
        private String accessKey;

        @Value("${spring.cloud.aws.credentials.secret-key}")
        private String secretKey;

        @Value("${spring.cloud.aws.region.static}")
        private String region;

        @Bean
        public S3Client s3Client() {
            AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
            return S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
        }
    }

    @Configuration
    @Profile("local") // "local" 프로필일 때만 이 설정을 사용 (로컬 테스트)
    public static class MockS3Config {

        @Bean
        public S3Client s3Client() {
            // 로컬 테스트용 더미 S3 클라이언트 생성
            // 실제 AWS 연결은 시도하지 않지만, 객체는 생성되어 의존성 주입 에러를 방지함
            AwsBasicCredentials credentials = AwsBasicCredentials.create("dummy", "dummy");
            return S3Client.builder()
                    .region(Region.AP_NORTHEAST_2)
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
        }
    }
}
