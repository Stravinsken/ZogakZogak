package com.example.PieceOfPeace.medication.dto.response;

import com.example.PieceOfPeace.medication.entity.Medication;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Schema(description = "약 알림 정보 응답 DTO")
public class MedicationResponse {

    @Schema(description = "약 알림 고유 ID")
    private final Long medicationId;

    @Schema(description = "약 이름")
    private final String pillName;

    @Schema(description = "알림 시간")
    private final LocalTime notificationTime;

    @Schema(description = "복용 여부")
    private final boolean isTaken;

    public static MedicationResponse from(Medication medication) {
        return new MedicationResponse(
                medication.getId(),
                medication.getPillName(),
                medication.getNotificationTime(),
                medication.isTaken()
        );
    }

}
