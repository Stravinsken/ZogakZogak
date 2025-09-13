package com.example.PieceOfPeace.diary.repository;

import com.example.PieceOfPeace.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findAllByWriterIdOrderByCreatedAtDesc(Long writerId);
}
