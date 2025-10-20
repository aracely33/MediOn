package clinica.medtech.medicalRecord.dtoRequest;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la creación o actualización de una entrada médica (MedicalEntry).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalEntryRequestDto {
    private LocalDateTime creationDate; // Fecha y hora de creación de la entrada médica
    private String type; // Tipo de entrada médica (consulta, evolución, etc.)
    private String summary; // Resumen de la entrada médica
    private String description; // Descripción detallada de la entrada médica
    private String observations; // Observaciones adicionales
    private String allergies; // Alergias registradas en la entrada
    private Long medicalRecordId; // ID de la historia clínica asociada
    private Long professionalId; // ID del profesional que realiza la entrada
    private List<Long> treatmentIds; // Lista de IDs de tratamientos asociados
    private List<Long> diagnosisIds; // Lista de IDs de diagnósticos asociados
}