package clinica.medtech.appointments.mapper;

import org.springframework.stereotype.Component;

import clinica.medtech.appointments.dto.request.AppointmentCreateRequest;
import clinica.medtech.appointments.dto.request.AppointmentUpdateRequest;
import clinica.medtech.appointments.dto.response.AppointmentResponse;
import clinica.medtech.appointments.entity.Appointment;
import clinica.medtech.appointments.enums.AppointmentStatus;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class AppointmentMapper {

    public Appointment toEntity(AppointmentCreateRequest request) {
        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .type(request.getType())
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .notes(request.getNotes())
                .status(AppointmentStatus.PENDIENTE)
                .build();

        return appointment;
    }

    public AppointmentResponse toResponse(Appointment appointment) {

        LocalDateTime endDateTime = null;
        if (appointment.getAppointmentDate() != null
                && appointment.getAppointmentTime() != null
               ) {

            endDateTime = LocalDateTime.of(
                    appointment.getAppointmentDate(),
                    appointment.getAppointmentTime()
            );

        }

        AppointmentResponse response = AppointmentResponse.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctorId())
                .patientId(appointment.getPatientId())
                .type(appointment.getType())
                .status(appointment.getStatus())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentEndDateTime(endDateTime)
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();

        return response;
    }

    public List<AppointmentResponse> toAppointmentListDto(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::toResponse)
                .toList();
    }

    public void updateFromDto(AppointmentUpdateRequest request, Appointment appointment) {
        if (request.getDoctorId() != null) {
            appointment.setDoctorId(request.getDoctorId());
        }

        if (request.getPatientId() != null) {
            appointment.setPatientId(request.getPatientId());
        }

        if (request.getType() != null) {
            appointment.setType(request.getType());
        }

        if (request.getReason() != null) {
            appointment.setReason(request.getReason());
        }

        if (request.getAppointmentDate() != null) {
            appointment.setAppointmentDate(request.getAppointmentDate());
        }

        if (request.getAppointmentTime() != null) {
            appointment.setAppointmentTime(request.getAppointmentTime());
        }


        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }

    }
}