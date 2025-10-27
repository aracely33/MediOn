package clinica.medtech.appointments.mapper;

import org.springframework.stereotype.Component;
import clinica.medtech.appointments.dto.request.AppointmentCreateRequest;
import clinica.medtech.appointments.dto.request.AppointmentUpdateRequest;
import clinica.medtech.appointments.dto.response.AppointmentResponse;
import clinica.medtech.appointments.entity.Appointment;
import clinica.medtech.appointments.enums.AppointmentStatus;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.entities.ProfessionalModel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class AppointmentMapper {

    public Appointment toEntity(AppointmentCreateRequest request, PatientModel patient, ProfessionalModel doctor) {
        return Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .type(request.getType())
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .notes(request.getNotes())
                .status(AppointmentStatus.PENDIENTE)
                .build();
    }

    public AppointmentResponse toResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctor().getId())
                .patientId(appointment.getPatient().getId())
                .type(appointment.getType())
                .status(appointment.getStatus())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentEndDateTime(appointment.getAppointmentEndDateTime()) // ✅ usando lógica del modelo
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }

    public void updateFromDto(AppointmentUpdateRequest request, Appointment appointment, ProfessionalModel doctor) {

        if (doctor != null) {
            appointment.setDoctor(doctor);
        }

        if (request.getType() != null) {
            appointment.setType(request.getType());
        }

        if (request.getReason() != null && !request.getReason().isBlank()) {
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
