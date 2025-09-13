package com.example.PieceOfPeace.diary.service;

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

    @Transactional
    public void createDiary(DiaryCreateRequest request, String writerEmail) {
        User writer = userRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        Diary diary = Diary.builder()
                .content(request.getContent())
                .date(request.getDate())
                .user(writer)
                .build();
        diaryRepository.save(diary);

        Emotion emotion = Emotion.builder()
                .sadness(request.getSadness())
                .anger(request.getAnger())
                .fear(request.getFear())
                .joy(request.getJoy())
                .happiness(request.getHappiness())
                .surprise(request.getSurprise())
                .diary(diary)
                .build();

        diary.setEmotion(emotion);
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

        if (!Objects.equals(diary.getUser().getId(), user.getId())) {
            throw new SecurityException("일기를 수정할 권한이 없습니다.");
        }

        diary.update(request.getContent(), request.getSadness(), request.getAnger(), request.getFear(), request.getJoy(), request.getHappiness(), request.getSurprise()); // getContents() -> getContent()
    }

    @Transactional
    public void deleteDiary(Long diaryId, String userEmail) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기를 찾을 수 없습니다."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        if (!Objects.equals(diary.getUser().getId(), user.getId())) {
            throw new SecurityException("일기를 삭제할 권한이 없습니다.");
        }

        diaryRepository.delete(diary);
    }
}
