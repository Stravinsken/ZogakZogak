package com.example.PieceOfPeace.user.dto;

import com.example.PieceOfPeace.user.entity.Senior;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SeniorResponseDto {
    private final Long id;
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
