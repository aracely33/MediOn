package clinica.medtech.medicalRecord.dtoResponse;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para exponer los datos de una entrada médica (MedicalEntry).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalEntryResponseDto {
    private Long id; // Identificador único de la entrada médica
    private LocalDateTime creationDate; // Fecha y hora de creación de la entrada
    private String type; // Tipo de entrada médica (consulta, evolución, etc.)
    private String summary; // Resumen de la entrada médica
    private String description; // Descripción detallada de la entrada médica
    private String observations; // Observaciones adicionales
    private String allergies; // Alergias registradas en la entrada
    private Long medicalRecordId; // ID de la historia clínica asociada
    private Long professionalId; // ID del profesional que realizó la entrada
    private List<TreatmentResponseDto> treatments; // Lista de tratamientos asociados a la entrada
    private List<DiagnosisResponseDto> diagnoses; // Lista de diagnósticos asociados a la entrada
}           