package clinica.medtech.users.mapper;

import clinica.medtech.users.dtoResponse.ProfessionalResponseDto;
import clinica.medtech.users.entities.ProfessionalModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfessionalMapper {

    public ProfessionalResponseDto mapToDto(ProfessionalModel professional) {
        return ProfessionalResponseDto.builder()
                .id(professional.getId())
                .name(professional.getName())
                .lastName(professional.getLastName())
                .email(professional.getEmail())
                .medicalLicense(professional.getMedicalLicense())
                .specialty(professional.getSpecialty())
                .biography(professional.getBiography())
                .consultationFee(professional.getConsultationFee())
                .build();
    }

    public List<ProfessionalResponseDto> mapToDtoList(List<ProfessionalModel> professionals) {
        return professionals.stream()
                .map(this::mapToDto)
                .toList();
    }


}
