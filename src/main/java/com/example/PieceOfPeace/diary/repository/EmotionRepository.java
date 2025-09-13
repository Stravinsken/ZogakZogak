package com.example.PieceOfPeace.diary.repository;

import com.example.PieceOfPeace.diary.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}
