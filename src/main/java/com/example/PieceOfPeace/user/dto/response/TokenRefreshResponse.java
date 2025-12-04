package com.example.PieceOfPeace.user.dto.response;

public record TokenRefreshResponse(
        String accessToken,
        String refreshToken
) {
}
