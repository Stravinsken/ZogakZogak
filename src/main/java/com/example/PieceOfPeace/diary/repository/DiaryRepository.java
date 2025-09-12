package com.example.PieceOfPeace.diary.repository;

import com.example.PieceOfPeace.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    // 사용자의 ID를 기반으로, 해당 사용자가 작성한 모든 일기를 생성된 시간의 내림차순(최신순)으로 조회
    List<Diary> findAllByWriterIdOrderByCreatedAtDesc(Long writerId);
}
