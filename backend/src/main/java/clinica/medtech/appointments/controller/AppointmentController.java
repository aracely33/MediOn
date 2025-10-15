package clinica.medtech.appointments.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import clinica.medtech.appointments.dto.request.CancelAppointmentDto;
import clinica.medtech.appointments.dto.request.ConfirmAppointmentDto;
import clinica.medtech.appointments.dto.request.CreateAppointmentDto;
import clinica.medtech.appointments.dto.request.UpdateAppointmentDto;
import clinica.medtech.appointments.dto.response.AppointmentDetailDto;
import clinica.medtech.appointments.dto.response.AppointmentResponseDto;
import clinica.medtech.appointments.dto.response.AppointmentSummaryDto;
import clinica.medtech.appointments.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Validated
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(@Valid @RequestBody CreateAppointmentDto dto) {
        AppointmentResponseDto created = appointmentService.createAppointment(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDetailDto> getById(@PathVariable Long id) {
        AppointmentDetailDto detail = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(detail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentDto dto) {
        // sincroniza id del path con dto
        dto.setId(id);
        AppointmentResponseDto updated = appointmentService.updateAppointment(dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(
            @PathVariable Long id,
            @RequestBody(required = false) ConfirmAppointmentDto dto) {
        // si el body es nulo, crear uno con el id
        if (dto == null) {
            dto = new ConfirmAppointmentDto();
        }
        dto.setAppointmentId(id);
        appointmentService.confirmAppointment(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            @RequestBody(required = false) CancelAppointmentDto dto) {
        if (dto == null) {
            dto = new CancelAppointmentDto();
        }
        dto.setAppointmentId(id);
        appointmentService.cancelAppointment(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentSummaryDto>> listByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.listByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentSummaryDto>> listByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.listByDoctor(doctorId));
    }
}