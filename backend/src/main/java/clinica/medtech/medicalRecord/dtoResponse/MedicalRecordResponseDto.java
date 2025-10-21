package clinica.medtech.medicalRecord.dtoResponse;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para exponer los datos de una historia clínica (MedicalRecord).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponseDto {
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private String observations;
    private Long patientId;
    private String patientName;
    private List<MedicalEntryResponseDto> entries;
}