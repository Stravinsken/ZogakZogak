package com.example.PieceOfPeace.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FamilyStatusResponseDto {
    private final boolean hasFamily;

    @Builder
    public FamilyStatusResponseDto(boolean hasFamily) {
        this.hasFamily = hasFamily;
    }
}
