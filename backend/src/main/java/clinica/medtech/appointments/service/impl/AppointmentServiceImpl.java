package clinica.medtech.appointments.service.impl;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import clinica.medtech.appointments.dto.request.CancelAppointmentDto;
import clinica.medtech.appointments.dto.request.ConfirmAppointmentDto;
import clinica.medtech.appointments.dto.request.CreateAppointmentDto;
import clinica.medtech.appointments.dto.request.UpdateAppointmentDto;
import clinica.medtech.appointments.dto.response.AppointmentDetailDto;
import clinica.medtech.appointments.dto.response.AppointmentResponseDto;
import clinica.medtech.appointments.dto.response.AppointmentSummaryDto;
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
public class AppointmentServiceImpl implements AppointmentService{
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    // estados que generan bloqueo/solapamiento
    private static final List<AppointmentStatus> CONFLICTING_STATUSES =
            List.of(AppointmentStatus.PENDIENTE, AppointmentStatus.CONFIRMADA, AppointmentStatus.EN_CURSO);

    @Override
    @Transactional
    public AppointmentResponseDto createAppointment(CreateAppointmentDto dto) {
        Appointment entity = appointmentMapper.toEntity(dto);

        if (entity.getDuration() == null) {
            entity.setDuration(30);
        }

        entity.setStatus(AppointmentStatus.PENDIENTE);

        // calcular endDateTime
        entity.setEndDateTime(entity.getAppointmentDateTime().plusMinutes(entity.getDuration()));

        boolean conflict = appointmentRepository.existsOverlapByDoctorAndTime(
                entity.getDoctorId(),
                entity.getAppointmentDateTime(),
                entity.getEndDateTime(),
                CONFLICTING_STATUSES
        );
        if (conflict) {
            throw new AppointmentConflictException("Conflicto de horario con otra cita.");
        }

        Appointment saved = appointmentRepository.save(entity);
        return appointmentMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDetailDto getAppointmentById(Long id) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada"));
        return appointmentMapper.toDetail(existing);
    }

    @Override
    @Transactional
    public AppointmentResponseDto updateAppointment(UpdateAppointmentDto dto) {
        Appointment existing = appointmentRepository.findById(dto.getId())
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada"));

        appointmentMapper.updateFromDto(dto, existing);

        if (existing.getDuration() == null) {
            existing.setDuration(30);
        }

        OffsetDateTime newStart = existing.getAppointmentDateTime();
        Integer newDuration = existing.getDuration();
        OffsetDateTime newEnd = newStart.plusMinutes(newDuration);
        existing.setEndDateTime(newEnd);

        boolean conflict = appointmentRepository.existsOverlapByDoctorAndTimeExcludingId(
                existing.getDoctorId(),
                newStart,
                newEnd,
                CONFLICTING_STATUSES,
                existing.getId()
        );
        if (conflict) {
            throw new AppointmentConflictException("Actualización genera conflicto con otra cita.");
        }

        Appointment saved = appointmentRepository.save(existing);
        return appointmentMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void confirmAppointment(ConfirmAppointmentDto dto) {
        Appointment existing = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada"));

        // comprobar solapamientos con otras citas antes de confirmar
        boolean conflict = appointmentRepository.existsOverlapByDoctorAndTimeExcludingId(
                existing.getDoctorId(),
                existing.getAppointmentDateTime(),
                existing.getEndDateTime(),
                CONFLICTING_STATUSES,
                existing.getId()
        );
        if (conflict) {
            throw new AppointmentConflictException("No es posible confirmar: existe conflicto con otra cita.");
        }

        existing.setStatus(AppointmentStatus.CONFIRMADA);
        if (dto.getConfirmationNotes() != null) {
            existing.setNotes(dto.getConfirmationNotes());
        }
        appointmentRepository.save(existing);
    }

    @Override
    @Transactional
    public void cancelAppointment(CancelAppointmentDto dto) {
        Appointment existing = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada"));

        existing.setStatus(AppointmentStatus.CANCELADA);
        if (dto.getReason() != null) {
            existing.setReason(dto.getReason());
        }
        appointmentRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSummaryDto> listByPatient(Long patientId) {
        List<Appointment> list = appointmentRepository.findByPatientIdOrderByAppointmentDateTimeDesc(patientId);
        return list.stream().map(appointmentMapper::toSummary).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSummaryDto> listByDoctor(Long doctorId) {
        List<Appointment> list = appointmentRepository.findByDoctorIdOrderByAppointmentDateTimeAsc(doctorId);
        return list.stream().map(appointmentMapper::toSummary).collect(Collectors.toList());
    }
}
