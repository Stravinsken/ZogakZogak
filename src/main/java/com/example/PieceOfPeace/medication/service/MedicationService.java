package com.example.PieceOfPeace.medication.service;

import com.example.PieceOfPeace.medication.dto.request.MedicationCreateRequest;
import com.example.PieceOfPeace.medication.dto.response.MedicationResponse;
import com.example.PieceOfPeace.medication.entity.Medication;
import com.example.PieceOfPeace.medication.repository.MedicationRepository;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Transactional
    public void createMedication(MedicationCreateRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        Medication medication = Medication.builder()
                .pillName(request.pillName())
                .notificationTime(LocalTime.parse(request.notificationTime(), TIME_FORMATTER))
                .user(user)
                .build();

        medicationRepository.save(medication);
    }

    public List<MedicationResponse> findMyMedications(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        List<Medication> medications = medicationRepository.findAllByUserOrderByNotificationTimeAsc(user);

        return medications.stream()
                .map(MedicationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markMedicationAsTaken(Long medicationId, String userEmail) {
        Medication medication = findMedicationAndVerifyOwner(medicationId, userEmail);
        medication.markAsTaken();
    }

    @Transactional
    public void deleteMedication(Long medicationId, String userEmail) {
        Medication medication = findMedicationAndVerifyOwner(medicationId, userEmail);
        medicationRepository.delete(medication);
    }

    private Medication findMedicationAndVerifyOwner(Long medicationId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 약 알림을 찾을 수 없습니다."));

        if (!Objects.equals(medication.getUser().getId(), user.getId())) {
            throw new SecurityException("해당 약 알림에 대한 권한이 없습니다.");
        }

        return medication;
    }
}
