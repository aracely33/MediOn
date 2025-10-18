package clinica.medtech.medicalRecord.dtoResponse;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * DTO para exponer la historia clínica en formato FHIR.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FhirMedicalRecordResponseDto {
    // Identificadores y metadatos FHIR
    private String id;
    private String number;
    private LocalDateTime creationDate;
    private String fhirId;
    private String observations;

    // Datos del paciente
    private String patientId;
    private String patientFhirId;
    private String patientName;
    private String patientDOB;
    private String patientSex;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String patientPhone;

    // Entradas médicas
    private List<MedicalEntryDto> medicalEntries;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicalEntryDto {
        private Long id;
        private LocalDateTime creationDate; // DOS
        private String type;
        private String summary;
        private String description;
        private String observations;
        private String allergies;
        private String fhirId;
        private String referringProvider; // Professional name
        private String chiefComplaint;
        private String medications;
        private String problemList;

        private List<DiagnosisDto> diagnoses;
        private List<TreatmentDto> treatments;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DiagnosisDto {
        private Long id;
        private String code;
        private String codeSystem;
        private String description;
        private Date startDate;
        private String severity;
        private String fhirId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TreatmentDto {
        private Long id;
        private String type;
        private String description;
        private String medication;
        private String dose;
        private String frequency;
        private Date start;
        private Date end;
        private String fhirId;
    }
}