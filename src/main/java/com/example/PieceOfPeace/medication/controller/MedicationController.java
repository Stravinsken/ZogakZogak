package com.example.PieceOfPeace.medication.controller;

import com.example.PieceOfPeace.medication.dto.request.MedicationCreateRequest;
import com.example.PieceOfPeace.medication.dto.response.MedicationResponse;
import com.example.PieceOfPeace.medication.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public ResponseEntity<Void> createMedication(@Valid @RequestBody MedicationCreateRequest request, Principal principal) {
        medicationService.createMedication(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<MedicationResponse>> findMyMedications(Principal principal) {
        List<MedicationResponse> response = medicationService.findMyMedications(principal.getName());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{medicationId}/take")
    public ResponseEntity<Void> markMedicationAsTaken(@PathVariable Long medicationId, Principal principal) {
        medicationService.markMedicationAsTaken(medicationId, principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{medicationId}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long medicationId, Principal principal) {
        medicationService.deleteMedication(medicationId, principal.getName());
        return ResponseEntity.ok().build();
    }
}
