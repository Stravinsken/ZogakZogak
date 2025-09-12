package com.example.PieceOfPeace.diary.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DiaryCreateRequest(
        @NotBlank(message = "일기 내용은 비워둘 수 없습니다.")
        String content
) {
}
