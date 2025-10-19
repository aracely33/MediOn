package clinica.medtech.appointments.service;


import java.time.LocalDateTime;
import java.util.List;

import clinica.medtech.appointments.dto.request.AppointmentCreateRequest;
import clinica.medtech.appointments.dto.request.AppointmentUpdateRequest;

import clinica.medtech.appointments.dto.response.AppointmentResponse;



public interface AppointmentService {

    AppointmentResponse scheduleAppointment(AppointmentCreateRequest dto);

    boolean updateAppointment(Long id, AppointmentUpdateRequest dto);

    boolean cancelAppointment(Long id, String reason);

    boolean confirmAppointment(Long id, String notes);

    List<LocalDateTime> getDoctorAvailability(Long doctorId);
}