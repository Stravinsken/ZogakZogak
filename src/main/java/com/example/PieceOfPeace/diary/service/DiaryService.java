package com.example.PieceOfPeace.diary.service;

import com.example.PieceOfPeace.analysis.dto.EmotionDto;
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

        // 1. 먼저 Diary 객체를 생성하고 저장하여 ID를 확보합니다.
        Diary diary = Diary.builder()
                .contents(request.getContents())
                .date(request.getDate())
                .user(writer)
                .build();
        diaryRepository.save(diary);

        // 2. 감정 분석 서비스를 호출하여 감정 점수를 받습니다.
        EmotionDto emotionDto = emotionAnalysisService.analyze(request.getContents());

        // 3. 받은 점수와 방금 생성된 Diary 객체로 Emotion 객체를 생성합니다.
        Emotion emotion = Emotion.builder()
                .sadness(emotionDto.getSadness())
                .anger(emotionDto.getAnger())
                .fear(emotionDto.getFear())
                .joy(emotionDto.getJoy())
                .happiness(emotionDto.getHappiness())
                .surprise(emotionDto.getSurprise())
                .diary(diary) // Emotion에 Diary를 연결합니다.
                .build();

        // 4. Diary에 Emotion을 연결합니다. (연관관계의 주인)
        diary.setEmotion(emotion);

        // Diary는 이미 save되었고, cascade 설정에 의해 Emotion도 함께 저장(또는 업데이트)됩니다.
    }

    public List<DiaryResponse> findMyDiaries(String writerEmail) {
        User writer = userRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        List<Diary> diaries = diaryRepository.findAllByUserOrderByDateDesc(writer);

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
        if (!Objects.equals(diary.getUser().getId(), user.getId())) {
            throw new SecurityException("일기를 수정할 권한이 없습니다.");
        }

        // 내용이 변경되었으므로 감정을 다시 분석
        EmotionDto newEmotionDto = emotionAnalysisService.analyze(request.getContents());

        // Diary 엔티티의 update 메소드를 사용하여 내용과 감정 점수를 한번에 업데이트
        diary.update(request.getContents(), newEmotionDto);
    }

    @Transactional
    public void deleteDiary(Long diaryId, String userEmail) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기를 찾을 수 없습니다."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        // 작성자 본인만 삭제할 수 있도록 권한 확인
        if (!Objects.equals(diary.getUser().getId(), user.getId())) {
            throw new SecurityException("일기를 삭제할 권한이 없습니다.");
        }

        diaryRepository.delete(diary);
    }
}
