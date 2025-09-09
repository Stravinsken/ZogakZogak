package com.example.PieceOfPeace.memory.repository;

import com.example.PieceOfPeace.memory.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

    List<Memory> findAllByWriterId(Long writerId);

    List<Memory> findAllByWriterIdOrderByCreatedAtDesc(Long writerId);

    /**
     * 키워드를 사용하여 기억의 제목, 내용 또는 작성자 이름에서 검색합니다.
     * @param keyword 검색할 키워드
     * @return 키워드를 포함하는 기억 목록
     */
    @Query("SELECT m FROM Memory m WHERE m.title LIKE %:keyword% OR m.content LIKE %:keyword% OR m.writer.name LIKE %:keyword%")
    List<Memory> searchByKeyword(@Param("keyword") String keyword);

}
