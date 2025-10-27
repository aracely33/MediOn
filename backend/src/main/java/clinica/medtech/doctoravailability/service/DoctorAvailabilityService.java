package clinica.medtech.doctoravailability.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import clinica.medtech.appointments.entity.Appointment;
import clinica.medtech.appointments.enums.AppointmentStatus;
import clinica.medtech.appointments.repository.AppointmentRepository;
import clinica.medtech.doctoravailability.entity.DoctorAvailability;
import clinica.medtech.doctoravailability.repository.DoctorAvailabilityRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorAvailabilityService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailabilityRepository availabilityRepository;


    private static final List<AppointmentStatus> BLOCKING_STATUSES =
            List.of(AppointmentStatus.PENDIENTE, AppointmentStatus.CONFIRMADA, AppointmentStatus.EN_CURSO);

    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {
        return getAvailableSlotsExcluding(doctorId, date, null);
    }

    public List<LocalTime> getAvailableSlotsExcluding(Long doctorId, LocalDate date, Long excludeAppointmentId) {

        DoctorAvailability config = availabilityRepository.findByDoctorIdAndIsActiveTrue(doctorId)
                .orElseGet(() -> DoctorAvailability.builder()
                        .doctorId(doctorId)
                        .morningStart(LocalTime.of(7, 0))
                        .morningEnd(LocalTime.of(12, 0))
                        .afternoonStart(LocalTime.of(14, 0))
                        .afternoonEnd(LocalTime.of(18, 0))
                        .build());

        List<LocalTime> allSlots = Stream.concat(
                generateSlots(config.getMorningStart(), config.getMorningEnd()).stream(),
                generateSlots(config.getAfternoonStart(), config.getAfternoonEnd()).stream()
        ).toList();

        List<Appointment> appointments = appointmentRepository.findActiveAppointmentsByDoctorAndDate(
                doctorId, date, BLOCKING_STATUSES
        );


        if (excludeAppointmentId != null) {
            appointments = appointments.stream()
                    .filter(a -> !a.getId().equals(excludeAppointmentId))
                    .toList();
        }

        Set<LocalTime> occupiedTimes = appointments.stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toSet());

        return allSlots.stream()
                .filter(slot -> !occupiedTimes.contains(slot))
                .toList();
    }

    private List<LocalTime> generateSlots(LocalTime start, LocalTime end) {
        List<LocalTime> slots = new ArrayList<>();
        while (start.isBefore(end)) {
            slots.add(start);
            start = start.plusMinutes(30);
        }
        return slots;
    }
}