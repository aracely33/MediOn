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
        if (dto.getStartTime().compareTo(dto.getEndTime()) >= 0) {
            throw new InvalidAvailabilityException("La hora de inicio debe ser anterior a la hora de fin");
        }

        boolean overlap = availabilityRepository.existsOverlap(dto.getDoctorId(), dto.getDayOfWeek(),
                dto.getStartTime(), dto.getEndTime());
        if (overlap) {
            throw new InvalidAvailabilityException("Existe una disponibilidad que se solapa con el rango proporcionado");
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

        if (dto.getStartTime().compareTo(dto.getEndTime()) >= 0) {
            throw new InvalidAvailabilityException("La hora de inicio debe ser anterior a la hora de fin");
        }

        // Usar la query que excluye el propio id para comprobar solapamiento
        boolean overlap = availabilityRepository.existsOverlapExcludingId(
                existing.getDoctorId(),
                existing.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime(),
                existing.getId()
        );
        if (overlap) {
            throw new InvalidAvailabilityException("La actualización genera solapamiento con otra disponibilidad");
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
        DoctorAvailability existing = availabilityRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new AvailabilityNotFoundException("Disponibilidad con id " + id + " no encontrada"));
        return availabilityMapper.toDto(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDto> listByDoctor(Long doctorId) {
        return availabilityRepository.findByDoctorIdAndIsActiveTrueOrderByDayOfWeekAscStartTimeAsc(doctorId)
                .stream().map(availabilityMapper::toDto).toList();
    }

    private boolean timesOverlap(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
        return aStart.isBefore(bEnd) && aEnd.isAfter(bStart);
    }
}
