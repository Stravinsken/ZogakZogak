package com.example.PieceOfPeace.diary.dto.request;

import lombok.Getter;

@Getter
public class DiaryUpdateRequest {
    private String content;

    private Double sadness;
    private Double anger;
    private Double fear;
    private Double joy;
    private Double happiness;
    private Double surprise;
}
