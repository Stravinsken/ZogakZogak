package com.example.PieceOfPeace.user.repository;

import com.example.PieceOfPeace.user.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyRepository extends JpaRepository<Family, Long> {

    List<Family> findBySeniorEmail(String seniorEmail);

    List<Family> findByGuardianEmail(String guardianEmail);
}
