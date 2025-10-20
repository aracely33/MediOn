package clinica.medtech.medicalRecord.dtoResponse;

import lombok.*;
import java.util.Date;

/**
 * DTO para exponer los datos de un diagnóstico (Diagnosis).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisResponseDto {
    private Long id;
    private String code;
    private String codeSystem;
    private String description;
    private Date startDate;
    private String severity;
    private Long medicalEntryId;
}