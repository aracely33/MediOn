package clinica.medtech.appointments.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
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
import clinica.medtech.doctoravailability.service.DoctorAvailabilityService;
import clinica.medtech.exceptions.PatientNotFoundException;
import clinica.medtech.exceptions.ProfessionalNotFoundException;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.entities.ProfessionalModel;
import clinica.medtech.users.repository.PatientRepository;
import clinica.medtech.users.repository.ProfessionalRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;
    private final ProfessionalRepository professionalRepository;
    private final DoctorAvailabilityService availabilityService;

    @Override
    public AppointmentResponse scheduleAppointment(AppointmentCreateRequest dto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        PatientModel patient = patientRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new PatientNotFoundException("No se encontró el paciente autenticado."));

        ProfessionalModel doctor = professionalRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ProfessionalNotFoundException("No se encontró el doctor especificado."));

        LocalDate today = LocalDate.now();
        if (dto.getAppointmentDate().isBefore(today)) {
            throw new AppointmentConflictException("No se puede agendar una cita en una fecha pasada.");
        }

        if (dto.getAppointmentDate().isEqual(today) && dto.getAppointmentTime().isBefore(LocalTime.now())) {
            throw new AppointmentConflictException("No se puede agendar una cita en una hora pasada.");
        }

        List<LocalTime> availableSlots =
                availabilityService.getAvailableSlots(doctor.getId(), dto.getAppointmentDate());

        if (!availableSlots.contains(dto.getAppointmentTime())) {
            throw new AppointmentConflictException("El horario seleccionado no está disponible para este doctor.");
        }

        Appointment appointment = appointmentMapper.toEntity(dto, patient, doctor);
        appointment.setStatus(AppointmentStatus.PENDIENTE);

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public boolean updateAppointment(Long id, AppointmentUpdateRequest dto) {

        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        ProfessionalModel doctor = existing.getDoctor();

        if (dto.getDoctorId() != null && !dto.getDoctorId().equals(doctor.getId())) {
            doctor = professionalRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new ProfessionalNotFoundException("No se encontró el doctor especificado."));
        }

        appointmentMapper.updateFromDto(dto, existing, doctor);

        LocalDate today = LocalDate.now();
        if (existing.getAppointmentDate().isBefore(today) ||
            (existing.getAppointmentDate().isEqual(today) && existing.getAppointmentTime().isBefore(LocalTime.now()))) {
            throw new AppointmentConflictException("No se puede reprogramar una cita a una fecha u hora pasada.");
        }

        if (dto.getAppointmentDate() != null || dto.getAppointmentTime() != null) {
        List<LocalTime> availableSlots =
                availabilityService.getAvailableSlotsExcluding(doctor.getId(), existing.getAppointmentDate(), existing.getId());

        if (!availableSlots.contains(existing.getAppointmentTime())) {
            throw new AppointmentConflictException("El nuevo horario no está disponible para este doctor.");
        }
    }
        appointmentRepository.save(existing);
        return true;
    }

    @Override
    public boolean cancelAppointment(Long id, String reason) {

        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        if (existing.getStatus() == AppointmentStatus.CANCELADA) {
            throw new AppointmentConflictException("La cita ya está cancelada.");
        }

        existing.setStatus(AppointmentStatus.CANCELADA);
        if (reason != null && !reason.isBlank()) existing.setReason(reason);

        appointmentRepository.save(existing);
        return true;
    }

    @Override
    public boolean confirmAppointment(Long id, String notes) {

        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        if (existing.getStatus() == AppointmentStatus.CANCELADA) {
            throw new AppointmentConflictException("No se puede confirmar una cita cancelada.");
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        if (existing.getAppointmentDate().isBefore(today) ||
           (existing.getAppointmentDate().isEqual(today) && existing.getAppointmentTime().isBefore(now))) {
            throw new AppointmentConflictException("No se puede confirmar una cita en una fecha u hora pasada.");
        }

        boolean conflict = appointmentRepository.existsOverlapByDoctorAndTimeExcludingId(
                existing.getDoctor().getId(),
                existing.getAppointmentDate(),
                existing.getAppointmentTime(),
                existing.getId(),
                List.of(AppointmentStatus.CONFIRMADA, AppointmentStatus.EN_CURSO)
        );

        if (conflict) {
            throw new AppointmentConflictException("No se puede confirmar porque el horario ya está ocupado.");
        }

        existing.setStatus(AppointmentStatus.CONFIRMADA);
        if (notes != null && !notes.isBlank()) existing.setNotes(notes);

        appointmentRepository.save(existing);
        return true;
    }


    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findActiveAppointmentsByPatientId(patientId)
                .stream().map(appointmentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsOfAuthenticatedPatient() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        PatientModel patient = patientRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new PatientNotFoundException("No se encontró el paciente autenticado."));

        return appointmentRepository.findActiveAppointmentsByPatientId(patient.getId())
                .stream().map(appointmentMapper::toResponse).toList();
    }
}

