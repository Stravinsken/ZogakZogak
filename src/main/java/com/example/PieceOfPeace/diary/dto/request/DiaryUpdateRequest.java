package com.example.PieceOfPeace.diary.dto.request;

import lombok.Getter;

@Getter
public class DiaryUpdateRequest {
    private String contents;

    // 프론트엔드에서 분석한 감정 점수를 받을 필드 추가
    private Double sadness;
    private Double anger;
    private Double fear;
    private Double joy;
    private Double happiness;
    private Double surprise;
}
