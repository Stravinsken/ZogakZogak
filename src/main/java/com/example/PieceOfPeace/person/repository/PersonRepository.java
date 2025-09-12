package com.example.PieceOfPeace.person.repository;

import com.example.PieceOfPeace.person.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    // Vector ID를 통해 인물을 조회 (핵심 기능)
    Optional<Person> findByVectorId(String vectorId);

    // 특정 사용자가 등록한 모든 인물 목록을 조회
    List<Person> findAllByWriterId(Long writerId);
}
