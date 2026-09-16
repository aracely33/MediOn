package clinica.medtech.medicalRecord.mapper;

import clinica.medtech.medicalRecord.dtoRequest.TreatmentRequestDto;
import clinica.medtech.medicalRecord.dtoResponse.TreatmentResponseDto;
import clinica.medtech.medicalRecord.entities.TreatmentModel;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre TreatmentModel y sus DTOs.
 */
@Component
public class TreatmentMapper {

    /**
     * Convierte TreatmentRequestDto a TreatmentModel.
     */
    public TreatmentModel toEntity(TreatmentRequestDto dto) {
        if (dto == null) return null;
        return TreatmentModel.builder()
                .type(dto.getType())
                .description(dto.getDescription())
                .medication(dto.getMedication())
                .dose(dto.getDose())
                .frequency(dto.getFrequency())
                .start(dto.getStart())
                .end(dto.getEnd())
                .build();
    }

    /**
     * Convierte TreatmentModel a TreatmentResponseDto.
     */
    public TreatmentResponseDto toResponseDto(TreatmentModel treatment) {
        if (treatment == null) return null;
        return TreatmentResponseDto.builder()
                .id(treatment.getId())
                .type(treatment.getType())
                .description(treatment.getDescription())
                .medication(treatment.getMedication())
                .dose(treatment.getDose())
                .frequency(treatment.getFrequency())
                .start(treatment.getStart())
                .end(treatment.getEnd())
                .medicalEntryId(treatment.getMedicalEntry() != null ? treatment.getMedicalEntry().getId() : null)
                .build();
    }
}
