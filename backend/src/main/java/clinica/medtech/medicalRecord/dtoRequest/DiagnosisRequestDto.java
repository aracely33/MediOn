package clinica.medtech.medicalRecord.dtoRequest;

import lombok.*;
import java.util.Date;

/**
 * DTO para la creación o actualización de un diagnóstico (Diagnosis).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisRequestDto {
    private String code;
    private String codeSystem;
    private String description;
    private Date startDate;
    private String severity;
    private Long medicalEntryId;
}