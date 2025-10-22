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

    private static final List<AppointmentStatus> CONFLICTING_STATUSES =
            List.of(AppointmentStatus.PENDIENTE, AppointmentStatus.CONFIRMADA, AppointmentStatus.EN_CURSO);

    //1. Agendar una nueva cita
    @Override
    public AppointmentResponse scheduleAppointment(AppointmentCreateRequest dto) {
        Appointment entity = appointmentMapper.toEntity(dto);

        // Validación de duración
        if (entity.getDuration() == null || entity.getDuration() < 15 || entity.getDuration() > 120) {
            throw new IllegalArgumentException("La duración debe estar entre 15 y 120 minutos.");
        }

        // Calculamos inicio y fin a partir de la fecha + hora
        LocalDateTime start = LocalDateTime.of(entity.getAppointmentDate(), entity.getAppointmentTime());
        LocalDateTime end = start.plusMinutes(entity.getDuration());

        // Verificamos conflicto de horario
        boolean conflict = appointmentRepository.existsOverlapByDoctorAndTime(
                entity.getDoctorId(),
                start,
                end,
                CONFLICTING_STATUSES
        );

        if (conflict) {
            throw new AppointmentConflictException("El doctor ya tiene otra cita en ese horario.");
        }

        entity.setStatus(AppointmentStatus.PENDIENTE);
        Appointment saved = appointmentRepository.save(entity);

        return appointmentMapper.toResponse(saved);
    }

    //2. Actualizar una cita existente
    @Override
    public boolean updateAppointment(Long id, AppointmentUpdateRequest dto) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        // Aplica solo los campos no nulos del DTO
        appointmentMapper.updateFromDto(dto, existing);

        // Si no hay fecha u hora, no recalculamos el horario
        if (existing.getAppointmentDate() == null || existing.getAppointmentTime() == null) {
            appointmentRepository.save(existing);
            return true;
        }

        // Asegura duración válida
        if (existing.getDuration() == null) {
            existing.setDuration(30);
        }

        LocalDateTime newStart = LocalDateTime.of(existing.getAppointmentDate(), existing.getAppointmentTime());
        LocalDateTime newEnd = newStart.plusMinutes(existing.getDuration());

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

    //3. Cancelar una cita
    @Override
    public boolean cancelAppointment(Long id, String reason) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        if (existing.getStatus() == AppointmentStatus.CANCELADA) {
            throw new AppointmentConflictException("La cita ya se encuentra cancelada.");
        }

        existing.setStatus(AppointmentStatus.CANCELADA);

        if (reason != null && !reason.isBlank()) {
            existing.setReason(reason);
        }

        appointmentRepository.save(existing);
        return true;
    }

    //4. Confirmar una cita
    @Override
    public boolean confirmAppointment(Long id, String notes) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        LocalDateTime start = LocalDateTime.of(existing.getAppointmentDate(), existing.getAppointmentTime());
        LocalDateTime end = start.plusMinutes(existing.getDuration() != null ? existing.getDuration() : 30);

        boolean conflict = appointmentRepository.existsOverlapByDoctorAndTimeExcludingId(
                existing.getDoctorId(),
                start,
                end,
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

    //5. Obtener la disponibilidad del doctor
    @Override
    public List<LocalDateTime> getDoctorAvailability(Long doctorId) {
        List<Appointment> activeAppointments = appointmentRepository.findActiveAppointmentsByDoctor(
                doctorId,
                CONFLICTING_STATUSES
        );

        return activeAppointments.stream()
                .map(a -> LocalDateTime.of(a.getAppointmentDate(), a.getAppointmentTime()))
                .collect(Collectors.toList());
    }
}