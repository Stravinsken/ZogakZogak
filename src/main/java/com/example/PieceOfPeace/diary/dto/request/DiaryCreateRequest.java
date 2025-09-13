package com.example.PieceOfPeace.diary.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DiaryCreateRequest {
    private String content;
    private LocalDate date;

    private Double sadness;
    private Double anger;
    private Double fear;
    private Double joy;
    private Double happiness;
    private Double surprise;
}
