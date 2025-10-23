package clinica.medtech.medicalRecord.dtoRequest;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

// ...existing code...

/**
 * DTO para la creación o actualización de un diagnóstico (Diagnosis).
 * Incluye validaciones para asegurar datos válidos antes de persistir.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisRequestDto {

    @NotBlank(message = "El código del diagnóstico es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9.\\-_/]+$", message = "Formato de código inválido")
    @Schema(description = "Código del diagnóstico (p.ej. CIE-10)", example = "K52.9")
    private String code;

    @NotBlank(message = "El sistema de codificación es obligatorio")
    @Size(max = 50, message = "El sistema de codificación no puede superar 50 caracteres")
    @Schema(description = "Sistema de codificación (p.ej. ICD-10)", example = "ICD-10")
    private String codeSystem;

    @NotBlank(message = "La descripción del diagnóstico es obligatoria")
    @Size(min = 5, max = 500, message = "La descripción debe tener entre 5 y 500 caracteres")
    @Schema(description = "Descripción del diagnóstico", example = "Gastroenteritis aguda, no especificada")
    private String description;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Schema(description = "Fecha de inicio del diagnóstico (ISO 8601)", example = "2025-10-19T00:00:00")
    private Date startDate;

    @NotBlank(message = "La severidad es obligatoria")
    @Pattern(regexp = "^(leve|moderada|grave|baja|alta)$", message = "Severidad inválida. Valores permitidos: leve, moderada, grave, baja, alta")
    @Schema(description = "Severidad del diagnóstico", example = "moderada")
    private String severity;

    @NotNull(message = "El ID de la entrada médica es obligatorio")
    @Positive(message = "El ID de la entrada médica debe ser un número positivo")
    @Schema(description = "ID de la entrada médica asociada", example = "10")
    private Long medicalEntryId;
}