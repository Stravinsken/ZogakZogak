package com.example.PieceOfPeace.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "가족 관계 생성 요청 DTO")
public record FamilyCreateRequest(
        @Schema(description = "어르신의 사용자 이메일", example = "senior@example.com")
        @NotBlank
        @Email
        String seniorUserEmail,

        @Schema(description = "어르신과의 관계", example = "딸")
        @NotBlank
        String relationship
) {
}
