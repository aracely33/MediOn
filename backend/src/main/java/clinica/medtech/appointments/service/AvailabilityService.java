package clinica.medtech.appointments.service;

import java.util.List;
import clinica.medtech.appointments.dto.request.CreateAvailabilityDto;
import clinica.medtech.appointments.dto.response.AvailabilityResponseDto;

public interface AvailabilityService {
    AvailabilityResponseDto createAvailability(CreateAvailabilityDto dto);

    AvailabilityResponseDto updateAvailability(Long id, CreateAvailabilityDto dto);

    void deactivateAvailability(Long id);

    AvailabilityResponseDto getAvailability(Long id);

    List<AvailabilityResponseDto> listByDoctor(Long doctorId);
}
