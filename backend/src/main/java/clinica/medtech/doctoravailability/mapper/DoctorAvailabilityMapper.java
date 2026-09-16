package clinica.medtech.doctoravailability.mapper;

import org.springframework.stereotype.Component;

import clinica.medtech.doctoravailability.dtos.request.CreateAvailabilityDto;
import clinica.medtech.doctoravailability.dtos.request.UpdateAvailabilityDto;
import clinica.medtech.doctoravailability.dtos.response.AvailabilityResponseDto;
import clinica.medtech.doctoravailability.entity.DoctorAvailability;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DoctorAvailabilityMapper {

    public DoctorAvailability toEntity(CreateAvailabilityDto dto) {

        DoctorAvailability entity = DoctorAvailability.builder()
                .doctorId(dto.getDoctorId())
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();

        return entity;
    }

    public AvailabilityResponseDto toDto(DoctorAvailability entity) {

        AvailabilityResponseDto response = AvailabilityResponseDto.builder()
                .id(entity.getId())
                .doctorId(entity.getDoctorId())
                .dayOfWeek(entity.getDayOfWeek())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .isActive(entity.getIsActive())
                .build();

        return response;
    }

    public void updateFromDto(UpdateAvailabilityDto dto, DoctorAvailability entity) {
        if (dto.getDayOfWeek() != null) {
            entity.setDayOfWeek(dto.getDayOfWeek());
        }

        if (dto.getStartTime() != null) {
            entity.setStartTime(dto.getStartTime());
        }

        if (dto.getEndTime() != null) {
            entity.setEndTime(dto.getEndTime());
        }

        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
    }
}