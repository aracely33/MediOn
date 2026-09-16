package clinica.medtech.medicalRecord.dtoRequest;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

// ...existing code...

/**
 * DTO para la creación o actualización de una historia clínica (MedicalRecord).
 * Contiene validaciones para garantizar datos consistentes.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordRequestDto {

    @NotBlank(message = "El número de la historia clínica es obligatorio")
    @Size(min = 3, max = 30, message = "El número debe tener entre 3 y 30 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\-_/]+$", message = "El número solo puede contener letras, números, guiones, guiones bajos o barras")
    @Schema(description = "Número identificador de la historia clínica", example = "HC-000123")
    private String number;

    @NotNull(message = "La fecha de creación es obligatoria")
    @Schema(description = "Fecha de creación de la historia clínica (ISO 8601, solo fecha)", example = "2025-10-19")
    private LocalDate creationDate;

    @Size(max = 1000, message = "Las observaciones no pueden superar 1000 caracteres")
    @Schema(description = "Observaciones generales de la historia clínica", example = "Paciente con antecedentes de hipertensión.")
    private String observations;

    @NotNull(message = "El ID del paciente es obligatorio")
    @Positive(message = "El ID del paciente debe ser un número positivo")
    @Schema(description = "ID del paciente asociado a la historia clínica", example = "1")
    private Long patientId;
}