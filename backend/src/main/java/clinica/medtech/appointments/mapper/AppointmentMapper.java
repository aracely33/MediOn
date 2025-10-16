package clinica.medtech.appointments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import clinica.medtech.appointments.dto.request.CreateAppointmentDto;
import clinica.medtech.appointments.dto.request.UpdateAppointmentDto;
import clinica.medtech.appointments.dto.response.AppointmentDetailDto;
import clinica.medtech.appointments.dto.response.AppointmentResponseDto;
import clinica.medtech.appointments.dto.response.AppointmentSummaryDto;
import clinica.medtech.appointments.entity.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mapping(target = "patientId", ignore = true) 
    Appointment toEntity(CreateAppointmentDto dto);

    void updateFromDto(UpdateAppointmentDto dto, @MappingTarget Appointment entity);

    AppointmentResponseDto toResponseDTO(Appointment entity);

    AppointmentDetailDto toDetail(Appointment entity);

    AppointmentSummaryDto toSummary(Appointment entity);
}
