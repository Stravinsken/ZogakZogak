package com.example.PieceOfPeace.memory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "기억 수정 요청 DTO")
public record MemoryUpdateRequest(
        @Schema(description = "새로운 기억 제목")
        @NotBlank
        String title,

        @Schema(description = "새로운 기억 내용")
        @NotBlank
        String content
) {
}
