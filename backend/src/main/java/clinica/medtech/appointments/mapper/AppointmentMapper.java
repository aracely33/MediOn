package clinica.medtech.appointments.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import clinica.medtech.appointments.dto.request.AppointmentCreateRequest;
import clinica.medtech.appointments.dto.request.AppointmentStatusUpdateRequest;
import clinica.medtech.appointments.dto.request.AppointmentUpdateRequest;
import clinica.medtech.appointments.dto.response.AppointmentResponse;
import clinica.medtech.appointments.dto.response.AppointmentSummaryResponse;
import clinica.medtech.appointments.entity.Appointment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentMapper {

    // ====== CREATE & UPDATE ======

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endDateTime", ignore = true) 
    @Mapping(target = "status", constant = "PENDIENTE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Appointment toEntity(AppointmentCreateRequest request);

    @Mapping(target = "endDateTime", ignore = true) 
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Appointment toEntity(AppointmentUpdateRequest request);

    // ====== RESPONSE ======

    AppointmentResponse toResponse(Appointment appointment);

    List<AppointmentResponse> toResponseList(List<Appointment> appointments);

    AppointmentSummaryResponse toSummary(Appointment appointment);

    List<AppointmentSummaryResponse> toSummaryList(List<Appointment> appointments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(AppointmentUpdateRequest request, @MappingTarget Appointment appointment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAppointmentFromStatus(AppointmentStatusUpdateRequest request, @MappingTarget Appointment appointment);
}