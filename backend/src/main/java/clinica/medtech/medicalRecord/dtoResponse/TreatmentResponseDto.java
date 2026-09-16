package clinica.medtech.medicalRecord.dtoResponse;

import lombok.*;
import java.util.Date;

/**
 * DTO para exponer los datos de un tratamiento (Treatment).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentResponseDto {
    private Long id;
    private String type;
    private String description;
    private String medication;
    private String dose;
    private String frequency;
    private Date start;
    private Date end;
    private Long medicalEntryId;
}