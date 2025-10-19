package clinica.medtech.doctoravailability.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import clinica.medtech.doctoravailability.dtos.request.CreateAvailabilityDto;
import clinica.medtech.doctoravailability.dtos.response.AvailabilityResponseDto;
import clinica.medtech.doctoravailability.entity.DoctorAvailability;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DoctorAvailabilityMapper {

    DoctorAvailability toEntity(CreateAvailabilityDto dto);

    AvailabilityResponseDto toDto(DoctorAvailability entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CreateAvailabilityDto dto, @MappingTarget DoctorAvailability entity);
}