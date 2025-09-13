package com.example.PieceOfPeace.diary.repository;

import com.example.PieceOfPeace.diary.entity.Diary;
import com.example.PieceOfPeace.user.entity.Senior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    // Senior를 기준으로 모든 일기를 날짜(date) 내림차순으로 조회하는 메소드로 변경
    List<Diary> findAllBySeniorOrderByDateDesc(Senior senior);

}
