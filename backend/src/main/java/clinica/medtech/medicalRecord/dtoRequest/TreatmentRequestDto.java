// ...existing code...
package clinica.medtech.medicalRecord.dtoRequest;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la creación o actualización de un tratamiento (Treatment).
 * Contiene validaciones básicas para asegurar la consistencia de los datos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentRequestDto {

    @NotBlank(message = "El tipo de tratamiento es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar 50 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9 ñÑÁÉÍÓÚáéíóú()\\-_.]{2,50}$", message = "Tipo contiene caracteres inválidos")
    @Schema(description = "Tipo de tratamiento", example = "Medicamento")
    private String type;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 5, max = 1000, message = "La descripción debe tener entre 5 y 1000 caracteres")
    @Schema(description = "Descripción del tratamiento", example = "Administración de analgésico")
    private String description;

    @Size(max = 100, message = "El nombre del medicamento no puede superar 100 caracteres")
    @Schema(description = "Medicamento indicado (si aplica)", example = "Paracetamol")
    private String medication;

    @Size(max = 50, message = "La dosis no puede superar 50 caracteres")
    @Schema(description = "Dosis indicada", example = "500mg")
    private String dose;

    @Size(max = 100, message = "La frecuencia no puede superar 100 caracteres")
    @Schema(description = "Frecuencia de administración", example = "Cada 8 horas")
    private String frequency;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Schema(description = "Fecha/hora de inicio del tratamiento (ISO 8601)", example = "2025-10-19T10:45:00")
    private Date start;

    @Schema(description = "Fecha/hora de fin del tratamiento (ISO 8601, opcional)", example = "2025-10-22T10:45:00")
    private Date end;

    @NotNull(message = "El ID de la entrada médica es obligatorio")
    @Positive(message = "El ID de la entrada médica debe ser un número positivo")
    @Schema(description = "ID de la entrada médica asociada", example = "1")
    private Long medicalEntryId;
}