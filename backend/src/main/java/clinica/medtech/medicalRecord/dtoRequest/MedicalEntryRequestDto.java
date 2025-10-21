// ...existing code...
package clinica.medtech.medicalRecord.dtoRequest;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la creación o actualización de una entrada médica (MedicalEntry).
 * Contiene validaciones para los campos más relevantes.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalEntryRequestDto {

    @NotNull(message = "La fecha de creación es obligatoria")
    @Schema(description = "Fecha de creación de la entrada médica (ISO 8601, solo fecha)", example = "2025-10-19")
    private LocalDate creationDate; // Fecha de creación de la entrada médica

    @NotBlank(message = "El tipo de entrada es obligatorio")
    @Size(min = 2, max = 50, message = "El tipo debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9 ñÑÁÉÍÓÚáéíóú()\\-_.]{2,50}$", message = "Tipo contiene caracteres inválidos")
    @Schema(description = "Tipo de entrada médica", example = "Consulta")
    private String type; // Tipo de entrada médica (consulta, evolución, etc.)

    @NotBlank(message = "El resumen es obligatorio")
    @Size(min = 5, max = 200, message = "El resumen debe tener entre 5 y 200 caracteres")
    @Schema(description = "Resumen breve de la entrada", example = "Consulta por dolor abdominal")
    private String summary; // Resumen de la entrada médica

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 5, max = 2000, message = "La descripción debe tener entre 5 y 2000 caracteres")
    @Schema(description = "Descripción detallada de la entrada", example = "El paciente refiere dolor abdominal desde hace 2 días.")
    private String description; // Descripción detallada de la entrada médica

    @Size(max = 500, message = "Las observaciones no pueden superar 500 caracteres")
    @Schema(description = "Observaciones adicionales", example = "Sin fiebre. Abdomen blando.")
    private String observations; // Observaciones adicionales

    @Size(max = 200, message = "El campo de alergias no puede superar 200 caracteres")
    @Schema(description = "Alergias registradas en la entrada", example = "Ninguna conocida")
    private String allergies; // Alergias registradas en la entrada

    @NotNull(message = "El ID de la historia clínica es obligatorio")
    @Positive(message = "El ID de la historia clínica debe ser un número positivo")
    @Schema(description = "ID de la historia clínica asociada", example = "1")
    private Long medicalRecordId; // ID de la historia clínica asociada

    @NotNull(message = "El ID del profesional es obligatorio")
    @Positive(message = "El ID del profesional debe ser un número positivo")
    @Schema(description = "ID del profesional que realiza la entrada", example = "2")
    private Long professionalId; // ID del profesional que realiza la entrada

    @Size(max = 100, message = "Máximo 100 IDs de tratamientos")
    @Schema(description = "Lista de IDs de tratamientos asociados", example = "[1, 2]")
    private List<@Positive(message = "Cada ID de tratamiento debe ser positivo") Long> treatmentIds; // Lista de IDs de tratamientos asociados

    @Size(max = 100, message = "Máximo 100 IDs de diagnósticos")
    @Schema(description = "Lista de IDs de diagnósticos asociados", example = "[10, 11]")
    private List<@Positive(message = "Cada ID de diagnóstico debe ser positivo") Long> diagnosisIds; // Lista de IDs de diagnósticos asociados
}
