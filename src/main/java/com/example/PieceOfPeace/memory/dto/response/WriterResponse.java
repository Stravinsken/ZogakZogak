package com.example.PieceOfPeace.memory.dto.response;

import com.example.PieceOfPeace.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "작성자 정보 응답 DTO")
public class WriterResponse {

    @Schema(description = "작성자 고유 ID")
    private final Long userId;

    @Schema(description = "작성자 이름")
    private final String name;

    @Builder
    public WriterResponse(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    // Entity를 DTO로 변환하는 정적 팩토리 메소드
    public static WriterResponse from(User user) {
        return WriterResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .build();
    }
}
