package com.example.PieceOfPeace.memory.repository;

import com.example.PieceOfPeace.memory.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

    /**
     * 특정 어르신(Senior)의 모든 추억을 추억 날짜(memoryDate)의 내림차순(최신순)으로 정렬하여 조회합니다.
     * @param seniorId 조회할 어르신의 ID
     * @return 날짜순으로 정렬된 추억 목록
     */
    List<Memory> findAllBySeniorIdOrderByMemoryDateDesc(Long seniorId);

}
