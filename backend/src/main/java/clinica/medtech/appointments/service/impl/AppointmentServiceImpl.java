package clinica.medtech.appointments.service.impl;

import java.util.List;
import java.util.Optional;

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

    private final List<AppointmentStatus> CONFLICTING_STATUSES = List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED);

    @Override
    @Transactional
    public AppointmentResponseDto createAppointment(CreateAppointmentDto dto) {
        // check basic conflicts (same datetime)
        boolean conflict = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(
                dto.getDoctorId(), dto.getAppointmentDateTime(), CONFLICTING_STATUSES);
        if (conflict) {
            throw new AppointmentConflictException("El médico ya tiene una cita en esa fecha/hora");
        }

        Appointment entity = appointmentMapper.toEntity(dto);
        entity.setStatus(AppointmentStatus.PENDING);
        Appointment saved = appointmentRepository.save(entity);
        return appointmentMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDetailDto getAppointmentById(Long id) {
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita con id " + id + " no encontrada"));
        return appointmentMapper.toDetail(a);
    }

    @Override
    @Transactional
    public AppointmentResponseDto updateAppointment(UpdateAppointmentDto dto) {
        Appointment a = appointmentRepository.findById(dto.getId())
                .orElseThrow(() -> new AppointmentNotFoundException("Cita con id " + dto.getId() + " no encontrada"));
        appointmentMapper.updateFromDto(dto, a);
        Appointment saved = appointmentRepository.save(a);
        return appointmentMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void confirmAppointment(ConfirmAppointmentDto dto) {
        Appointment a = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException("Cita con id " + dto.getAppointmentId() + " no encontrada"));

        // check conflict before confirming
        boolean conflict = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(
                a.getDoctorId(), a.getAppointmentDateTime(), CONFLICTING_STATUSES);
        // if a itself is in DB with PENDING this will be true; only consider conflict if another appointment exists
        if (conflict) {
            // attempt to find an appointment at same date/time other than this one
            Optional<Appointment> other = appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(
                    a.getDoctorId(), a.getAppointmentDateTime().minusSeconds(1), a.getAppointmentDateTime().plusSeconds(1))
                    .stream()
                    .filter(ap -> !ap.getId().equals(a.getId()))
                    .findAny();
            if (other.isPresent()) {
                throw new AppointmentConflictException("No se puede confirmar: existe otra cita en la misma fecha/hora");
            }
        }

        a.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(a);
    }

    @Override
    @Transactional
    public void cancelAppointment(CancelAppointmentDto dto) {
        Appointment a = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException("Cita con id " + dto.getAppointmentId() + " no encontrada"));
        a.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(a);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSummaryDto> listByPatient(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateTimeDesc(patientId)
                .stream().map(appointmentMapper::toSummary).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSummaryDto> listByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateTimeAsc(doctorId)
                .stream().map(appointmentMapper::toSummary).toList();
    }
}
