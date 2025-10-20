package clinica.medtech.medicalRecord.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Código del diagnóstico")
    private String code;
    @Schema(description = "Sistema del diagnóstico")
    private String codeSystem;
    @Schema(description = "Descripción del diagnóstico")
    private String description;
    @Schema(description = "Fecha de inicio del diagnóstico")
    private Date startDate;
    @Schema(description = "Fecha de fin del diagnóstico")
    private String severity;
    @Schema(description = "Id de la entrada medica")
    private Long medicalEntryId;
}