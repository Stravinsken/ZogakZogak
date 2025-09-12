package com.example.PieceOfPeace.analysis.service;

import com.example.PieceOfPeace.diary.entity.Emotion;
import org.springframework.stereotype.Service;

@Service
public class EmotionAnalysisService {

    /**
     * 주어진 텍스트의 감정을 분석합니다.
     * (현재는 임시 구현으로 항상 NEUTRAL을 반환합니다.)
     * @param text 분석할 텍스트
     * @return 분석된 감정
     */
    public Emotion analyze(String text) {
        // TODO: 실제 감정 분석 AI 모델 연동 로직 구현
        System.out.println("감정 분석 요청: " + text);
        System.out.println("분석 결과 (임시): NEUTRAL");
        return Emotion.NEUTRAL;
    }
}
