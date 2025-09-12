package com.example.PieceOfPeace.person.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PersonRegisterRequest(
        @Schema(description = "Gemini API가 생성한 고유 벡터 ID", example = "gemini-vector-id-123")
        @NotBlank(message = "Vector ID는 필수입니다.")
        String vectorId,

        @Schema(description = "인물의 이름", example = "김철수")
        @NotBlank(message = "이름은 비워둘 수 없습니다.")
        String name,

        @Schema(description = "등록자와 인물의 관계", example = "아들")
        @NotBlank(message = "관계는 비워둘 수 없습니다.")
        String relationship
) {
}
