package clinica.medtech.appointments.service.impl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clinica.medtech.appointments.dto.request.AppointmentCreateRequest;
import clinica.medtech.appointments.dto.request.AppointmentUpdateRequest;

import clinica.medtech.appointments.dto.response.AppointmentResponse;
import clinica.medtech.appointments.entity.Appointment;
import clinica.medtech.appointments.enums.AppointmentStatus;
import clinica.medtech.appointments.exception.AppointmentConflictException;
import clinica.medtech.appointments.exception.AppointmentNotFoundException;
import clinica.medtech.appointments.mapper.AppointmentMapper;
import clinica.medtech.appointments.repository.AppointmentRepository;
import clinica.medtech.appointments.service.AppointmentService;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    // Estados de cita que bloquean disponibilidad
    private static final List<AppointmentStatus> CONFLICTING_STATUSES =
            List.of(AppointmentStatus.PENDIENTE, AppointmentStatus.CONFIRMADA, AppointmentStatus.EN_CURSO);

    @Override
    public AppointmentResponse scheduleAppointment(AppointmentCreateRequest dto) {
        Appointment entity = appointmentMapper.toEntity(dto);

        boolean conflict = appointmentRepository.existsOverlapByDoctorAndTime(
                entity.getDoctorId(),
                entity.getAppointmentDateTime(),
                entity.getAppointmentDateTime().plusMinutes(entity.getDuration()),
                CONFLICTING_STATUSES
        );

        if (conflict) {
            throw new AppointmentConflictException("El doctor ya tiene otra cita en ese horario.");
        }

        entity.setStatus(AppointmentStatus.PENDIENTE);
        Appointment saved = appointmentRepository.save(entity);

        return appointmentMapper.toResponse(saved);
    }

    @Override
    public boolean updateAppointment(Long id, AppointmentUpdateRequest dto) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        appointmentMapper.updateFromDto(dto, existing);

        if (existing.getAppointmentDateTime() == null) {
            return true;
        }

        if (existing.getDuration() == null) {
            existing.setDuration(30);
        }

        LocalDateTime newStart = existing.getAppointmentDateTime();
        LocalDateTime newEnd = newStart.plusMinutes(existing.getDuration());
        existing.setEndDateTime(newEnd);

        boolean conflict = appointmentRepository.existsOverlapByDoctorAndTimeExcludingId(
                existing.getDoctorId(),
                newStart,
                newEnd,
                CONFLICTING_STATUSES,
                existing.getId()
        );

        if (conflict) {
            throw new AppointmentConflictException("La nueva hora entra en conflicto con otra cita existente.");
        }

        appointmentRepository.save(existing);
        return true;
    }

    @Override
    public boolean cancelAppointment(Long id, String reason) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        existing.setStatus(AppointmentStatus.CANCELADA);

        if (reason != null && !reason.isBlank()) {
            existing.setReason(reason);
        }

        appointmentRepository.save(existing);
        return true;
    }

    @Override
    public boolean confirmAppointment(Long id, String notes) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        boolean conflict = appointmentRepository.existsOverlapByDoctorAndTimeExcludingId(
                existing.getDoctorId(),
                existing.getAppointmentDateTime(),
                existing.getEndDateTime(),
                CONFLICTING_STATUSES,
                existing.getId()
        );

        if (conflict) {
            throw new AppointmentConflictException("No se puede confirmar la cita debido a un conflicto de horario.");
        }

        existing.setStatus(AppointmentStatus.CONFIRMADA);

        if (notes != null && !notes.isBlank()) {
            existing.setNotes(notes);
        }

        appointmentRepository.save(existing);
        return true;
    }


    @Override
    public List<LocalDateTime> getDoctorAvailability(Long doctorId) {
        List<Appointment> activeAppointments = appointmentRepository.findActiveAppointmentsByDoctor(
                doctorId,
                CONFLICTING_STATUSES // PENDIENTE, CONFIRMADA, EN_CURSO
        );

        return activeAppointments.stream()
                .map(Appointment::getAppointmentDateTime)
                .collect(Collectors.toList());
    }

}