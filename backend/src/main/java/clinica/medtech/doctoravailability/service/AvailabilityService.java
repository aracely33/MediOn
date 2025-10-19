package clinica.medtech.doctoravailability.service;

import java.util.List;

import clinica.medtech.doctoravailability.dtos.request.CreateAvailabilityDto;
import clinica.medtech.doctoravailability.dtos.response.AvailabilityResponseDto;

public interface AvailabilityService {

    AvailabilityResponseDto createAvailability(CreateAvailabilityDto dto);

    AvailabilityResponseDto updateAvailability(Long id, CreateAvailabilityDto dto);

    void deactivateAvailability(Long id);

    AvailabilityResponseDto getAvailability(Long id);

    List<AvailabilityResponseDto> listByDoctor(Long doctorId);
}