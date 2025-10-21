package clinica.medtech.medicalRecord.dtoRequest;

import lombok.*;
import java.util.Date;

/**
 * DTO para la creación o actualización de un tratamiento (Treatment).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentRequestDto {
    private String type;
    private String description;
    private String medication;
    private String dose;
    private String frequency;
    private Date start;
    private Date end;
    private Long medicalEntryId;
}