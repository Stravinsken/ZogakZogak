package com.example.PieceOfPeace.user.dto.request;

import com.example.PieceOfPeace.user.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "회원가입 요청 DTO")
public record RegisterRequest(
        @Schema(description = "사용자 이메일", example = "test@example.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "비밀번호", example = "password123")
        @NotBlank
        String password,

        @Schema(description = "사용자 이름", example = "김평화")
        @NotBlank
        String name,

        @Schema(description = "역할 (SENIOR 또는 GUARDIAN)", example = "SENIOR")
        @NotNull
        UserRole role
) {
}
