package com.example.PieceOfPeace.medication.repository;

import com.example.PieceOfPeace.medication.entity.Medication;
import com.example.PieceOfPeace.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findAllByUserOrderByNotificationTimeAsc(User user);

}
