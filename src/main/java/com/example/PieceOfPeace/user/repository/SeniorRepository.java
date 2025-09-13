package com.example.PieceOfPeace.user.repository;

import com.example.PieceOfPeace.user.entity.Senior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeniorRepository extends JpaRepository<Senior, Long> {
    // 보호자(User) ID로 모든 어르신 프로필을 찾는 기능
    List<Senior> findAllByGuardianId(Long guardianId);
}
