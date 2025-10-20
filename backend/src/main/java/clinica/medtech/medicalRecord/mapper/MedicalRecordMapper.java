package clinica.medtech.medicalRecord.mapper;

import clinica.medtech.medicalRecord.dtoResponse.MedicalEntryResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.MedicalRecordResponseDto;
import clinica.medtech.medicalRecord.entities.MedicalRecordModel;
import clinica.medtech.users.entities.PatientModel;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre MedicalRecordModel y MedicalRecordResponseDto.
 */
@Component
public class MedicalRecordMapper {

    private final MedicalEntryMapper medicalEntryMapper;

    public MedicalRecordMapper(MedicalEntryMapper medicalEntryMapper) {
        this.medicalEntryMapper = medicalEntryMapper;
    }
    /**
     * Convierte una entidad MedicalRecordModel a su DTO de respuesta.
     */
    public MedicalRecordResponseDto toResponseDto(MedicalRecordModel model) {
        if (model == null) return null;
        PatientModel patient = model.getPatient();
        return MedicalRecordResponseDto.builder()
                .id(model.getId())
                .number(model.getNumber())
                .creationDate(model.getCreationDate())
                .observations(model.getObservations())
                .patientId(patient != null ? patient.getId() : null)
                .patientName(patient != null ? patient.getName() + " " + patient.getLastName() : null)
                .build();
    }
        /**
     * Convierte una entidad MedicalRecordModel a su DTO de respuesta, incluyendo las entradas médicas.
     */
    public MedicalRecordResponseDto toResponseDto2(MedicalRecordModel model) {
        if (model == null) return null;
        PatientModel patient = model.getPatient();

        List<MedicalEntryResponseDto> entries = model.getMedicalEntries() != null
                ? model.getMedicalEntries().stream()
                    .map(medicalEntryMapper::toResponseDto2) // Usa el método que mapea tratamientos y diagnósticos
                    .toList()
                : List.of();

        return MedicalRecordResponseDto.builder()
                .id(model.getId())
                .number(model.getNumber())
                .creationDate(model.getCreationDate())
                .observations(model.getObservations())
                .patientId(patient != null ? patient.getId() : null)
                .patientName(patient != null ? patient.getName() + " " + patient.getLastName() : null)
                .entries(entries)
                .build();
    }
}