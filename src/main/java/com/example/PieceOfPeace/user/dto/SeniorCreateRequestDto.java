package com.example.PieceOfPeace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SeniorCreateRequestDto {
    @Schema(description = "등록할 어르신의 이름", example = "김철수")
    private String name;
}
