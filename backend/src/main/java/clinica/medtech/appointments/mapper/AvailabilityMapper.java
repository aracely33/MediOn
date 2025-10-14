package clinica.medtech.appointments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import clinica.medtech.appointments.dto.request.CreateAvailabilityDto;
import clinica.medtech.appointments.dto.response.AvailabilityResponseDto;
import clinica.medtech.appointments.entity.DoctorAvailability;

@Mapper(componentModel = "spring")
public interface AvailabilityMapper {
    DoctorAvailability toEntity(CreateAvailabilityDto dto);

    AvailabilityResponseDto toDto(DoctorAvailability entity);

    void updateFromDto(CreateAvailabilityDto dto, @MappingTarget DoctorAvailability entity);
}
