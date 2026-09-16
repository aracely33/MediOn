package clinica.medtech.medicalRecord.mapper;

import clinica.medtech.medicalRecord.dtoRequest.MedicalEntryRequestDto;
import clinica.medtech.medicalRecord.dtoResponse.DiagnosisResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.MedicalEntryResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.TreatmentResponseDto;
import clinica.medtech.medicalRecord.entities.MedicalEntryModel;

import java.util.List;

import org.springframework.stereotype.Component;
import clinica.medtech.medicalRecord.entities.TreatmentModel;
import clinica.medtech.medicalRecord.entities.DiagnosisModel;
/**
 * Mapper para convertir entre MedicalEntryModel y sus DTOs.
 */
@Component
public class MedicalEntryMapper {

    private final TreatmentMapper treatmentMapper;
    private final DiagnosisMapper diagnosisMapper;
    public MedicalEntryMapper(TreatmentMapper treatmentMapper, DiagnosisMapper diagnosisMapper) {
        this.treatmentMapper = treatmentMapper;
        this.diagnosisMapper = diagnosisMapper;
    }

    /**
     * Convierte un DTO de request a entidad MedicalEntryModel.
     */
    public MedicalEntryModel toEntity(MedicalEntryRequestDto dto) {
        if (dto == null) return null;
        return MedicalEntryModel.builder()
                .creationDate(dto.getCreationDate())
                .type(dto.getType())
                .summary(dto.getSummary())
                .description(dto.getDescription())
                .observations(dto.getObservations())
                .allergies(dto.getAllergies())
                // Puedes agregar aquí el mapeo de medicalRecord, professional, treatments y diagnoses si tienes los objetos
                .build();
    }

    /**
     * Convierte una entidad MedicalEntryModel a su DTO de respuesta.
     */
    public MedicalEntryResponseDto toResponseDto(MedicalEntryModel entry) {
        if (entry == null) return null;
        return MedicalEntryResponseDto.builder()
                .id(entry.getId())
                .creationDate(entry.getCreationDate())
                .type(entry.getType())
                .summary(entry.getSummary())
                .description(entry.getDescription())
                .observations(entry.getObservations())
                .allergies(entry.getAllergies())
                .medicalRecordId(entry.getMedicalRecord() != null ? entry.getMedicalRecord().getId() : null)
                .professionalId(entry.getProfessional() != null ? entry.getProfessional().getId() : null)
                
                .build();
    }
    public MedicalEntryResponseDto toResponseDto2(MedicalEntryModel entry) {
        if (entry == null) return null;
        // Mapear tratamientos
        List<TreatmentResponseDto> treatments = entry.getTreatments() != null
                ? entry.getTreatments().stream()
                    .map(treatmentMapper::toResponseDto)
                    .toList()
                : List.of();
        // Mapear diagnósticos
        List<DiagnosisResponseDto> diagnoses = entry.getDiagnoses() != null
                ? entry.getDiagnoses().stream()
                    .map(diagnosisMapper::toResponseDto)
                    .toList()
                : List.of();

        return MedicalEntryResponseDto.builder()
                .id(entry.getId())
                .creationDate(entry.getCreationDate())
                .type(entry.getType())
                .summary(entry.getSummary())
                .description(entry.getDescription())
                .observations(entry.getObservations())
                .allergies(entry.getAllergies())
                .medicalRecordId(entry.getMedicalRecord() != null ? entry.getMedicalRecord().getId() : null)
                .professionalId(entry.getProfessional() != null ? entry.getProfessional().getId() : null)
                .treatments(treatments)
                .diagnoses(diagnoses)
                .build();
    }
}