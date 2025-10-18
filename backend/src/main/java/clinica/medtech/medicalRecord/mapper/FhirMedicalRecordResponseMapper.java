package clinica.medtech.medicalRecord.mapper;

import clinica.medtech.medicalRecord.dtoResponse.FhirMedicalRecordResponseDto;
import clinica.medtech.medicalRecord.entities.*;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.entities.ProfessionalModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper para convertir entidades del modelo de dominio a FhirMedicalRecordResponseDto,
 * que representa la historia clínica completa en formato FHIR para interoperabilidad.
 */
@Component
public class FhirMedicalRecordResponseMapper {

    /**
     * Convierte una entidad MedicalRecordModel a su DTO FHIR correspondiente.
     *
     * @param entity Entidad MedicalRecordModel.
     * @return DTO FhirMedicalRecordResponseDto con todos los datos mapeados.
     */
    public FhirMedicalRecordResponseDto toDto(MedicalRecordModel entity) {
        if (entity == null) return null;

        PatientModel patient = entity.getPatient();

        return FhirMedicalRecordResponseDto.builder()
                .id(entity.getFhirId())
                .number(entity.getNumber())
                .creationDate(entity.getCreationDate())
                .fhirId(entity.getFhirId())
                .observations(entity.getObservations())
                // Datos del paciente
                .patientId(patient != null ? String.valueOf(patient.getId()) : null)
                .patientFhirId(patient != null ? patient.getFhirId() : null)
                .patientName(patient != null ? patient.getName() + " " + patient.getLastName() : null)
                .patientDOB(patient != null && patient.getBirthDate() != null ? patient.getBirthDate().toString() : null)
                .patientSex(patient != null ? patient.getGender() : null)
                .address(patient != null ? patient.getAddress() : null)
                .city(patient != null ? patient.getCity() : null)
                .state(patient != null ? patient.getState() : null)
                .zip(patient != null ? patient.getZipCode() : null)
                .patientPhone(patient != null ? patient.getPhone() : null)
                // Entradas médicas
                .medicalEntries(entity.getMedicalEntries() != null
                        ? entity.getMedicalEntries().stream()
                            .map(this::toMedicalEntryDto)
                            .collect(Collectors.toList())
                        : null)
                .build();
    }

    /**
     * Convierte una entidad MedicalEntryModel a su DTO FHIR correspondiente.
     *
     * @param entry Entidad MedicalEntryModel.
     * @return DTO MedicalEntryDto con diagnósticos y tratamientos.
     */
    private FhirMedicalRecordResponseDto.MedicalEntryDto toMedicalEntryDto(MedicalEntryModel entry) {
        ProfessionalModel professional = entry.getProfessional();

        return FhirMedicalRecordResponseDto.MedicalEntryDto.builder()
                .id(entry.getId())
                .creationDate(entry.getCreationDate())
                .type(entry.getType())
                .summary(entry.getSummary())
                .description(entry.getDescription())
                .observations(entry.getObservations())
                .allergies(entry.getAllergies())
                .fhirId(entry.getFhirId())
                .referringProvider(professional != null ? professional.getName() + " " + professional.getLastName() : null)
                .chiefComplaint(entry.getChiefComplaint())
                .medications(entry.getMedications())
                .problemList(entry.getProblemList())
                // Diagnósticos
                .diagnoses(entry.getDiagnoses() != null
                        ? entry.getDiagnoses().stream()
                            .map(this::toDiagnosisDto)
                            .collect(Collectors.toList())
                        : null)
                // Tratamientos
                .treatments(entry.getTreatments() != null
                        ? entry.getTreatments().stream()
                            .map(this::toTreatmentDto)
                            .collect(Collectors.toList())
                        : null)
                .build();
    }

    /**
     * Convierte una entidad DiagnosisModel a su DTO FHIR correspondiente.
     *
     * @param diagnosis Entidad DiagnosisModel.
     * @return DTO DiagnosisDto.
     */
    private FhirMedicalRecordResponseDto.DiagnosisDto toDiagnosisDto(DiagnosisModel diagnosis) {
        return FhirMedicalRecordResponseDto.DiagnosisDto.builder()
                .id(diagnosis.getId())
                .code(diagnosis.getCode())
                .codeSystem(diagnosis.getCodeSystem())
                .description(diagnosis.getDescription())
                .startDate(diagnosis.getStartDate())
                .severity(diagnosis.getSeverity())
                .fhirId(diagnosis.getFhirId())
                .build();
    }

    /**
     * Convierte una entidad TreatmentModel a su DTO FHIR correspondiente.
     *
     * @param treatment Entidad TreatmentModel.
     * @return DTO TreatmentDto.
     */
    private FhirMedicalRecordResponseDto.TreatmentDto toTreatmentDto(TreatmentModel treatment) {
        return FhirMedicalRecordResponseDto.TreatmentDto.builder()
                .id(treatment.getId())
                .type(treatment.getType())
                .description(treatment.getDescription())
                .medication(treatment.getMedication())
                .dose(treatment.getDose())
                .frequency(treatment.getFrequency())
                .start(treatment.getStart())
                .end(treatment.getEnd())
                .fhirId(treatment.getFhirId())
                .build();
    }
}