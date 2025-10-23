package clinica.medtech.doctoravailability.service.impl;


import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clinica.medtech.doctoravailability.dtos.request.CreateAvailabilityDto;
import clinica.medtech.doctoravailability.dtos.request.UpdateAvailabilityDto;
import clinica.medtech.doctoravailability.dtos.response.AvailabilityResponseDto;
import clinica.medtech.doctoravailability.entity.DoctorAvailability;
import clinica.medtech.doctoravailability.exception.InvalidAvailabilityException;
import clinica.medtech.doctoravailability.exception.ResourceNotFoundException;
import clinica.medtech.doctoravailability.mapper.DoctorAvailabilityMapper;
import clinica.medtech.doctoravailability.repository.DoctorAvailabilityRepository;
import clinica.medtech.doctoravailability.service.AvailabilityService;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final DoctorAvailabilityRepository repository;
    private final DoctorAvailabilityMapper mapper;

    @Override
    @Transactional
    public AvailabilityResponseDto createAvailability(CreateAvailabilityDto dto) {
        validateTimes(dto.getStartTime(), dto.getEndTime());

        DoctorAvailability entity = mapper.toEntity(dto);
        DoctorAvailability saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
@Transactional
public AvailabilityResponseDto updateAvailability(Long id, UpdateAvailabilityDto dto) {
    DoctorAvailability existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Disponibilidad no encontrada con id: " + id));

    if (dto.getStartTime() != null && dto.getEndTime() != null) {
        validateTimes(dto.getStartTime(), dto.getEndTime());
    }

    mapper.updateFromDto(dto, existing);
    DoctorAvailability updated = repository.save(existing);

    return mapper.toDto(updated);
}

    @Override
    @Transactional
    public void deactivateAvailability(Long id) {
        DoctorAvailability existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disponibilidad no encontrada con id: " + id));

        existing.setIsActive(false);
        repository.save(existing);
    }

    @Override
@Transactional(readOnly = true)
public AvailabilityResponseDto getAvailability(Long id) {
    DoctorAvailability availability = repository
            .findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Disponibilidad no encontrada o inactiva con id: " + id));

    return mapper.toDto(availability);
}

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDto> listByDoctor(Long doctorId) {
        return repository.findByDoctorIdAndIsActiveTrue(doctorId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    private void validateTimes(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new InvalidAvailabilityException("Las horas de inicio y fin son obligatorias.");
        }
        if (!start.isBefore(end)) {
            throw new InvalidAvailabilityException("La hora de inicio debe ser anterior a la hora de fin.");
        }
    }
}