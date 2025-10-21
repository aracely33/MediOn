package clinica.medtech.medicalRecord.mapper;

import clinica.medtech.medicalRecord.dtoResponse.PatientMedicalRecordResponseDto;
import clinica.medtech.medicalRecord.entities.MedicalEntryModel;
import clinica.medtech.medicalRecord.entities.MedicalRecordModel;
import clinica.medtech.users.entities.PatientModel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Mapper para convertir PatientModel y su historia clínica a PatientMedicalRecordResponseDto.
 */
@Component
public class PatientMedicalRecordResponseMapper {

    /**
     * Convierte un PatientModel y su historia clínica a un DTO de respuesta.
     */
    public PatientMedicalRecordResponseDto toDto(PatientModel patient) {
        if (patient == null) return null;

        MedicalRecordModel record = patient.getMedicalRecord();

        return PatientMedicalRecordResponseDto.builder()
                .patientId(patient.getId())
                .fhirId(patient.getFhirId())
                .name(patient.getName())
                .lastName(patient.getLastName())
                .birthDate(patient.getBirthDate())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .city(patient.getCity())
                .country(patient.getCountry())
                .zip(patient.getZip())
                .phone(patient.getPhone())
                .bloodType(patient.getBloodType())
                .medicalRecords(record != null
                        ? Collections.singletonList(toMedicalRecordSummaryDto(record))
                        : Collections.emptyList())
                .build();
    }

    /**
     * Convierte una historia clínica a su DTO resumen.
     */
    private PatientMedicalRecordResponseDto.MedicalRecordSummaryDto toMedicalRecordSummaryDto(MedicalRecordModel record) {
        return PatientMedicalRecordResponseDto.MedicalRecordSummaryDto.builder()
                .id(record.getId())
                .number(record.getNumber())
                .fhirId(record.getFhirId())
                .observations(record.getObservations())
                .medicalEntries(record.getMedicalEntries() != null
                        ? record.getMedicalEntries().stream()
                            .map(this::toMedicalEntrySummaryDto)
                            .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    /**
     * Convierte una entrada médica a su DTO resumen.
     */
    private PatientMedicalRecordResponseDto.MedicalEntrySummaryDto toMedicalEntrySummaryDto(MedicalEntryModel entry) {
        return PatientMedicalRecordResponseDto.MedicalEntrySummaryDto.builder()
                .id(entry.getId())
                .type(entry.getType())
                .summary(entry.getSummary())
                .description(entry.getDescription())
                .observations(entry.getObservations())
                .allergies(entry.getAllergies())
                /* .chiefComplaint(entry.getChiefComplaint())
                .medications(entry.getMedications())
                .problemList(entry.getProblemList()) */
                .build();
    }
}