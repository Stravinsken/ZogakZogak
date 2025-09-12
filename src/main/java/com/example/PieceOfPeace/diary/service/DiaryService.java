package com.example.PieceOfPeace.diary.service;

import com.example.PieceOfPeace.analysis.service.EmotionAnalysisService;
import com.example.PieceOfPeace.diary.dto.request.DiaryCreateRequest;
import com.example.PieceOfPeace.diary.dto.request.DiaryUpdateRequest;
import com.example.PieceOfPeace.diary.dto.response.DiaryResponse;
import com.example.PieceOfPeace.diary.entity.Diary;
import com.example.PieceOfPeace.diary.entity.Emotion;
import com.example.PieceOfPeace.diary.repository.DiaryRepository;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final EmotionAnalysisService emotionAnalysisService;

    @Transactional
    public void createDiary(DiaryCreateRequest request, String writerEmail) {
        User writer = userRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        // 감정 분석 서비스 호출
        Emotion emotion = emotionAnalysisService.analyze(request.content());

        Diary diary = Diary.builder()
                .content(request.content())
                .emotion(emotion)
                .writer(writer)
                .build();

        diaryRepository.save(diary);
    }

    public List<DiaryResponse> findMyDiaries(String writerEmail) {
        User writer = userRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        List<Diary> diaries = diaryRepository.findAllByWriterIdOrderByCreatedAtDesc(writer.getId());

        return diaries.stream()
                .map(DiaryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateDiary(Long diaryId, DiaryUpdateRequest request, String userEmail) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기를 찾을 수 없습니다."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        // 작성자 본인만 수정할 수 있도록 권한 확인
        if (!Objects.equals(diary.getWriter().getId(), user.getId())) {
            throw new SecurityException("일기를 수정할 권한이 없습니다.");
        }

        // 내용이 변경되었으므로 감정을 다시 분석
        Emotion newEmotion = emotionAnalysisService.analyze(request.content());
        diary.update(request.content(), newEmotion);
    }

    @Transactional
    public void deleteDiary(Long diaryId, String userEmail) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기를 찾을 수 없습니다."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        // 작성자 본인만 삭제할 수 있도록 권한 확인
        if (!Objects.equals(diary.getWriter().getId(), user.getId())) {
            throw new SecurityException("일기를 삭제할 권한이 없습니다.");
        }

        diaryRepository.delete(diary);
    }
}
