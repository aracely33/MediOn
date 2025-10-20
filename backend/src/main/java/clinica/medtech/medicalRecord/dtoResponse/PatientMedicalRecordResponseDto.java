package clinica.medtech.medicalRecord.dtoResponse;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para exponer los datos del paciente junto con todas sus historias clínicas y entradas médicas.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientMedicalRecordResponseDto {
    private Long patientId;
    private String fhirId;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String city;
    private String country;
    private String zip;
    private String phone;
    private String bloodType;

    private List<MedicalRecordSummaryDto> medicalRecords;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicalRecordSummaryDto {
        private Long id;
        private String number;
        private String fhirId;
        private String observations;
        private List<MedicalEntrySummaryDto> medicalEntries;
    }

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
    }
}