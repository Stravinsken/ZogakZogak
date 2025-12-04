package com.example.PieceOfPeace.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답 DTO")
public record LoginResponse(
        @Schema(description = "액세스 토큰")
        String accessToken,
        @Schema(description = "리프레시 토큰")
        String refreshToken
) {
}
