package clinica.medtech.medicalRecord.mapper;

import clinica.medtech.medicalRecord.dtoRequest.DiagnosisRequestDto;
import clinica.medtech.medicalRecord.dtoResponse.DiagnosisResponseDto;
import clinica.medtech.medicalRecord.entities.DiagnosisModel;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre DiagnosisModel y sus DTOs.
 */
@Component
public class DiagnosisMapper {

    /**
     * Convierte DiagnosisRequestDto a DiagnosisModel.
     */
    public DiagnosisModel toEntity(DiagnosisRequestDto dto) {
        if (dto == null) return null;
        return DiagnosisModel.builder()
                .code(dto.getCode())
                .codeSystem(dto.getCodeSystem())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .severity(dto.getSeverity())
                .build();
    }

    /**
     * Convierte DiagnosisModel a DiagnosisResponseDto.
     */
    public DiagnosisResponseDto toResponseDto(DiagnosisModel diagnosis) {
        if (diagnosis == null) return null;
        return DiagnosisResponseDto.builder()
                .id(diagnosis.getId())
                .code(diagnosis.getCode())
                .codeSystem(diagnosis.getCodeSystem())
                .description(diagnosis.getDescription())
                .startDate(diagnosis.getStartDate())
                .severity(diagnosis.getSeverity())
                .medicalEntryId(diagnosis.getMedicalEntry() != null ? diagnosis.getMedicalEntry().getId() : null)
                .build();
    }
}