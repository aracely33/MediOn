package clinica.medtech.doctoravailability.service;

import java.util.List;

import clinica.medtech.doctoravailability.dtos.request.CreateAvailabilityDto;
import clinica.medtech.doctoravailability.dtos.request.UpdateAvailabilityDto;
import clinica.medtech.doctoravailability.dtos.response.AvailabilityResponseDto;

public interface AvailabilityService {

    AvailabilityResponseDto createAvailability(CreateAvailabilityDto dto);

    AvailabilityResponseDto updateAvailability(Long id, UpdateAvailabilityDto dto);

    void deactivateAvailability(Long id);

    AvailabilityResponseDto getAvailability(Long id);

    List<AvailabilityResponseDto> listByDoctor(Long doctorId);
}