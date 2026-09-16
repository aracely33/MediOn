package clinica.medtech.medicalRecord.dtoResponse;

import lombok.*;
import java.util.List;

/**
 * DTO para exponer los datos del médico junto con todas las entradas médicas que ha realizado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionalMedicalRecordResponseDto {
    private Long professionalId;
    private String name;
    private String lastName;
    private String specialty;
    private String licenseNumber;
    private String email;
    private String phone;

    private List<MedicalEntrySummaryDto> medicalEntries;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicalEntrySummaryDto {
        private Long id;
        private String type;
        private String summary;
        private String description;
        private String observations;
        private String allergies;
        private String chiefComplaint;
        private String medications;
        private String problemList;
        private Long patientId;
        private String patientName;
    }
}