package com.example.PieceOfPeace.diary.service;

import com.example.PieceOfPeace.diary.dto.request.DiaryCreateRequest;
import com.example.PieceOfPeace.diary.dto.request.DiaryUpdateRequest;
import com.example.PieceOfPeace.diary.dto.response.DiaryResponse;
import com.example.PieceOfPeace.diary.entity.Diary;
import com.example.PieceOfPeace.diary.entity.Emotion;
import com.example.PieceOfPeace.diary.repository.DiaryRepository;
import com.example.PieceOfPeace.user.entity.Senior;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.SeniorRepository;
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
    private final SeniorRepository seniorRepository;

    @Transactional
    public void createDiary(DiaryCreateRequest request, User guardian) {
        // 1. 요청받은 seniorId로 Senior(어르신 프로필)를 찾습니다.
        Senior senior = findSeniorById(request.getSeniorId());

        // 2. (보안) 찾은 Senior가 현재 로그인한 보호자(guardian)의 소유가 맞는지 확인합니다.
        validateSeniorOwnership(senior, guardian);

        // 3. Diary 객체를 생성합니다.
        Diary diary = Diary.builder()
                .content(request.getContent())
                .date(request.getDate())
                .senior(senior) // Diary의 주인을 Senior로 설정
                .build();
        diaryRepository.save(diary);

        // 4. DTO로부터 직접 받은 감정 점수로 Emotion 객체를 생성합니다.
        Emotion emotion = Emotion.builder()
                .sadness(request.getSadness())
                .anger(request.getAnger())
                .fear(request.getFear())
                .joy(request.getJoy())
                .happiness(request.getHappiness())
                .surprise(request.getSurprise())
                .diary(diary)
                .build();

        // 5. Diary에 Emotion을 연결합니다. (cascade 설정에 의해 Emotion도 함께 저장됩니다)
        diary.setEmotion(emotion);
    }

    public List<DiaryResponse> findDiariesBySenior(Long seniorId, User guardian) {
        // 1. 요청받은 seniorId로 Senior를 찾습니다.
        Senior senior = findSeniorById(seniorId);

        // 2. (보안) 찾은 Senior가 현재 로그인한 보호자의 소유가 맞는지 확인합니다.
        validateSeniorOwnership(senior, guardian);

        // 3. 해당 Senior의 모든 일기를 날짜 역순으로 조회합니다.
        List<Diary> diaries = diaryRepository.findAllBySeniorOrderByDateDesc(senior);

        return diaries.stream()
                .map(DiaryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateDiary(Long diaryId, DiaryUpdateRequest request, User guardian) {
        Diary diary = findDiaryById(diaryId);

        // (보안) 이 일기의 최종 소유자인 보호자가 현재 로그인한 보호자가 맞는지 확인합니다.
        validateSeniorOwnership(diary.getSenior(), guardian);

        diary.update(request.getContent(), request.getSadness(), request.getAnger(), request.getFear(), request.getJoy(), request.getHappiness(), request.getSurprise());
    }

    @Transactional
    public void deleteDiary(Long diaryId, User guardian) {
        Diary diary = findDiaryById(diaryId);

        // (보안) 이 일기의 최종 소유자인 보호자가 현재 로그인한 보호자가 맞는지 확인합니다.
        validateSeniorOwnership(diary.getSenior(), guardian);

        diaryRepository.delete(diary);
    }

    // --- Private Helper Methods ---

    private Diary findDiaryById(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기를 찾을 수 없습니다."));
    }

    private Senior findSeniorById(Long seniorId) {
        return seniorRepository.findById(seniorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 어르신 프로필을 찾을 수 없습니다."));
    }

    private void validateSeniorOwnership(Senior senior, User guardian) {
        if (!Objects.equals(senior.getGuardian().getId(), guardian.getId())) {
            throw new SecurityException("해당 어르신 프로필에 접근할 권한이 없습니다.");
        }
    }
}
