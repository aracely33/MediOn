package clinica.medtech.medicalRecord.dtoRequest;

import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO para la creación o actualización de una historia clínica (MedicalRecord).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordRequestDto {
    private String number;
    private LocalDateTime creationDate;
    private String observations;
    private Long patientId;
}