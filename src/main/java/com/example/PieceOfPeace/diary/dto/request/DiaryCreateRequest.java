package com.example.PieceOfPeace.diary.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DiaryCreateRequest {
    private Long seniorId; // 어떤 어르신의 일기인지 식별하기 위한 ID
    private String content;
    private LocalDate date;

    private Double sadness;
    private Double anger;
    private Double fear;
    private Double joy;
    private Double happiness;
    private Double surprise;
}
