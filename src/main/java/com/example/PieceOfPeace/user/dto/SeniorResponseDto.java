package com.example.PieceOfPeace.user.dto;

import com.example.PieceOfPeace.user.entity.Senior;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "어르신 정보 응답 DTO")
public class SeniorResponseDto {

    @Schema(description = "어르신 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "어르신 이름", example = "이영희")
    private final String name;

    @Builder
    public SeniorResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static SeniorResponseDto from(Senior senior) {
        return SeniorResponseDto.builder()
                .id(senior.getId())
                .name(senior.getName())
                .build();
    }
}
