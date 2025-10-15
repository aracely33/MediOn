package clinica.medtech.appointments.service.impl;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import clinica.medtech.appointments.dto.request.CreateAvailabilityDto;
import clinica.medtech.appointments.dto.response.AvailabilityResponseDto;
import clinica.medtech.appointments.entity.DoctorAvailability;
import clinica.medtech.appointments.exception.AvailabilityNotFoundException;
import clinica.medtech.appointments.exception.InvalidAvailabilityException;
import clinica.medtech.appointments.mapper.AvailabilityMapper;
import clinica.medtech.appointments.repository.DoctorAvailabilityRepository;
import clinica.medtech.appointments.service.AvailabilityService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService{
        private final DoctorAvailabilityRepository availabilityRepository;
    private final AvailabilityMapper availabilityMapper;

    @Override
    @Transactional
    public AvailabilityResponseDto createAvailability(CreateAvailabilityDto dto) {
        validateTimes(dto.getStartTime(), dto.getEndTime());

        boolean exists = availabilityRepository.existsByDoctorIdAndDayOfWeekAndStartTimeAndEndTime(
            dto.getDoctorId(), dto.getDayOfWeek(), dto.getStartTime(), dto.getEndTime()
        );
        if (exists) {
            throw new InvalidAvailabilityException("Ya existe una disponibilidad idéntica para este doctor y día");
        }

        DoctorAvailability entity = availabilityMapper.toEntity(dto);
        DoctorAvailability saved = availabilityRepository.save(entity);
        return availabilityMapper.toDto(saved);
    }

    @Override
    @Transactional
    public AvailabilityResponseDto updateAvailability(Long id, CreateAvailabilityDto dto) {
        DoctorAvailability existing = availabilityRepository.findById(id)
            .orElseThrow(() -> new AvailabilityNotFoundException("Disponibilidad con id " + id + " no encontrada"));

        validateTimes(dto.getStartTime(), dto.getEndTime());

        // Si hay una disponibilidad idéntica diferente, evitar duplicado
        boolean exists = availabilityRepository.existsByDoctorIdAndDayOfWeekAndStartTimeAndEndTime(
            dto.getDoctorId(), dto.getDayOfWeek(), dto.getStartTime(), dto.getEndTime()
        );
        if (exists && !(existing.getDayOfWeek().equals(dto.getDayOfWeek())
                && existing.getStartTime().equals(dto.getStartTime())
                && existing.getEndTime().equals(dto.getEndTime())
        )) {
            throw new InvalidAvailabilityException("Ya existe una disponibilidad idéntica para este doctor y día");
        }

        availabilityMapper.updateFromDto(dto, existing);
        DoctorAvailability saved = availabilityRepository.save(existing);
        return availabilityMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deactivateAvailability(Long id) {
        DoctorAvailability existing = availabilityRepository.findById(id)
            .orElseThrow(() -> new AvailabilityNotFoundException("Disponibilidad con id " + id + " no encontrada"));
        existing.setIsActive(false);
        availabilityRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public AvailabilityResponseDto getAvailability(Long id) {
        DoctorAvailability existing = availabilityRepository.findById(id)
            .orElseThrow(() -> new AvailabilityNotFoundException("Disponibilidad con id " + id + " no encontrada"));
        return availabilityMapper.toDto(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDto> listByDoctor(Long doctorId) {
        return availabilityRepository.findByDoctorIdAndIsActiveTrue(doctorId)
            .stream().map(availabilityMapper::toDto).toList();
    }

    private void validateTimes(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new InvalidAvailabilityException("Hora de inicio y de fin son obligatorias");
        }
        if (!start.isBefore(end)) {
            throw new InvalidAvailabilityException("La hora de inicio debe ser anterior a la hora de fin");
        }
    }
}
