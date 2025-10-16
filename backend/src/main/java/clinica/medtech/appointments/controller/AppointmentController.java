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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Appointments API", description = "Gestión de citas médicas (crear, actualizar, confirmar, cancelar, listar).")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(
        summary = "Crear una nueva cita",
        description = "Permite a un paciente programar una cita con un médico.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Cita creada exitosamente",
                    content = @Content(schema = @Schema(implementation = AppointmentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(@Valid @RequestBody CreateAppointmentDto dto) {
        AppointmentResponseDto created = appointmentService.createAppointment(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(
        summary = "Obtener detalles de una cita",
        description = "Devuelve la información detallada de una cita específica por su ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cita encontrada",
                    content = @Content(schema = @Schema(implementation = AppointmentDetailDto.class))),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDetailDto> getById(@PathVariable Long id) {
        AppointmentDetailDto detail = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(detail);
    }

    @Operation(
        summary = "Actualizar una cita",
        description = "Permite modificar los datos de una cita existente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = AppointmentResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada", content = @Content)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentDto dto) {
        // sincroniza id del path con dto
        dto.setId(id);
        AppointmentResponseDto updated = appointmentService.updateAppointment(dto);
        return ResponseEntity.ok(updated);
    }
/*  
    @Operation(
        summary = "Confirmar una cita",
        description = "Cambia el estado de una cita a 'CONFIRMADA' si no hay conflictos con otras citas.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Cita confirmada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada", content = @Content)
        }
    )
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

    @Operation(
        summary = "Cancelar una cita",
        description = "Cambia el estado de una cita a 'CANCELADA'.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Cita cancelada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada", content = @Content)
        }
    )
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

    @Operation(
        summary = "Listar citas de un paciente",
        description = "Obtiene todas las citas asociadas a un paciente ordenadas por fecha descendente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Listado de citas obtenido correctamente",
                    content = @Content(schema = @Schema(implementation = AppointmentSummaryDto.class)))
        }
    )
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentSummaryDto>> listByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.listByPatient(patientId));
    }
*/
    @Operation(
        summary = "Listar citas de un médico",
        description = "Obtiene todas las citas asociadas a un médico ordenadas por fecha ascendente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Listado de citas obtenido correctamente",
                    content = @Content(schema = @Schema(implementation = AppointmentSummaryDto.class)))
        }
    )
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentSummaryDto>> listByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.listByDoctor(doctorId));
    }
}