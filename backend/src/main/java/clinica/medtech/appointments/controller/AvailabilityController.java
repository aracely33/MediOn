package clinica.medtech.appointments.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import clinica.medtech.appointments.dto.request.CreateAvailabilityDto;
import clinica.medtech.appointments.dto.response.AvailabilityResponseDto;
import clinica.medtech.appointments.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
@Validated
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<AvailabilityResponseDto> create(@Valid @RequestBody CreateAvailabilityDto dto) {
        AvailabilityResponseDto created = availabilityService.createAvailability(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateAvailabilityDto dto) {
        AvailabilityResponseDto updated = availabilityService.updateAvailability(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        availabilityService.deactivateAvailability(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDto> get(@PathVariable Long id) {
        AvailabilityResponseDto dto = availabilityService.getAvailability(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AvailabilityResponseDto>> listByDoctor(@PathVariable Long doctorId) {
        List<AvailabilityResponseDto> list = availabilityService.listByDoctor(doctorId);
        return ResponseEntity.ok(list);
    }
}
