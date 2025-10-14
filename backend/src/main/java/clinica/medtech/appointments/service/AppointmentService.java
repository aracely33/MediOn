package clinica.medtech.appointments.service;

import java.util.List;

import clinica.medtech.appointments.dto.request.CancelAppointmentDto;
import clinica.medtech.appointments.dto.request.ConfirmAppointmentDto;
import clinica.medtech.appointments.dto.request.CreateAppointmentDto;
import clinica.medtech.appointments.dto.request.UpdateAppointmentDto;
import clinica.medtech.appointments.dto.response.AppointmentDetailDto;
import clinica.medtech.appointments.dto.response.AppointmentResponseDto;
import clinica.medtech.appointments.dto.response.AppointmentSummaryDto;

public interface AppointmentService {
    AppointmentResponseDto createAppointment(CreateAppointmentDto dto);
    AppointmentDetailDto getAppointmentById(Long id);
    AppointmentResponseDto updateAppointment(UpdateAppointmentDto dto);
    void confirmAppointment(ConfirmAppointmentDto dto);
    void cancelAppointment(CancelAppointmentDto dto);
    List<AppointmentSummaryDto> listByPatient(Long patientId);
    List<AppointmentSummaryDto> listByDoctor(Long doctorId);
}
