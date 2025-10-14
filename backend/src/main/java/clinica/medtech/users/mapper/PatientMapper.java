package clinica.medtech.users.mapper;

import clinica.medtech.users.dtoResponse.PatientMeResponseDto;
import clinica.medtech.users.entities.PatientModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientMapper {

    public PatientMeResponseDto mapToDto(PatientModel patient) {
        PatientMeResponseDto dto = new PatientMeResponseDto();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setLastName(patient.getLastName());
        dto.setEmail(patient.getEmail());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());
        dto.setPhone(patient.getPhone());
        dto.setBirthDate(patient.getBirthDate());
        dto.setBloodType(patient.getBloodType());
        return dto;
    }

    public List<PatientMeResponseDto> mapToDtoList(List<PatientModel> patients) {
        return patients.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
