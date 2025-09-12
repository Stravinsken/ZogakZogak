package com.example.PieceOfPeace.person.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PersonUpdateRequest(
        @NotBlank(message = "이름은 비워둘 수 없습니다.")
        String name,

        @NotBlank(message = "관계는 비워둘 수 없습니다.")
        String relationship
) {
}
