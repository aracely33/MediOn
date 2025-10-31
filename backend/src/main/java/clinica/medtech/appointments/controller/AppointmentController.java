package clinica.medtech.appointments.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import clinica.medtech.appointments.dto.request.AppointmentCreateRequest;
import clinica.medtech.appointments.dto.request.AppointmentUpdateRequest;
import clinica.medtech.appointments.dto.response.AppointmentResponse;
import clinica.medtech.appointments.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Validated
@Tag(
    name = "API de Citas Médicas",
    description = "Operaciones para gestionar las citas médicas de pacientes autenticados."
)
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(
        summary = "Agendar una nueva cita",
        description = "Permite al paciente autenticado crear una nueva cita médica."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cita creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o formato incorrecto"),
        @ApiResponse(responseCode = "401", description = "No autorizado (token inválido o ausente)"),
        @ApiResponse(responseCode = "409", description = "Conflicto al programar la cita (fecha pasada o doctor ocupado)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<AppointmentResponse> scheduleAppointment(
            @Valid @RequestBody AppointmentCreateRequest request) {

        AppointmentResponse created = appointmentService.scheduleAppointment(request);
        URI location = URI.create("/api/appointments/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(
        summary = "Actualizar parcialmente una cita",
        description = "Permite modificar los campos de una cita existente como fecha, hora o motivo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cita actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o formato incorrecto"),
        @ApiResponse(responseCode = "404", description = "No se encontró la cita a actualizar"),
        @ApiResponse(responseCode = "409", description = "El nuevo horario no está disponible"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateAppointment(
            @Parameter(description = "ID de la cita a actualizar", example = "12") @PathVariable Long id,
            @Valid @RequestBody AppointmentUpdateRequest request) {

        appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Cancelar una cita",
        description = "Cancela una cita existente cambiando su estado a CANCELADA."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cita cancelada correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró la cita"),
        @ApiResponse(responseCode = "409", description = "La cita ya estaba cancelada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(
            @Parameter(description = "ID de la cita a cancelar", example = "13") @PathVariable Long id,
            @Parameter(description = "Motivo de cancelación", example = "Paciente no podrá asistir")
            @RequestParam(required = false) String reason) {

        appointmentService.cancelAppointment(id, reason);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Confirmar una cita",
        description = "Confirma una cita médica si el horario sigue disponible para el doctor."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cita confirmada correctamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "409", description = "Conflicto al confirmar (horario ocupado o cita cancelada)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmAppointment(
            @Parameter(description = "ID de la cita a confirmar", example = "13") @PathVariable Long id,
            @Parameter(description = "Notas adicionales", example = "Confirmada por paciente vía app")
            @RequestParam(required = false) String notes) {

        appointmentService.confirmAppointment(id, notes);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Ver mis citas médicas",
        description = "Obtiene todas las citas activas del paciente autenticado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de citas obtenida correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado (token inválido o ausente)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/me")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments() {
        return ResponseEntity.ok(appointmentService.getAppointmentsOfAuthenticatedPatient());
    }

    @Operation(
        summary = "Obtener cita por ID",
        description = "Obtiene los detalles de una cita específica del paciente autenticado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cita encontrada correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado (token inválido o ausente)"),
        @ApiResponse(responseCode = "403", description = "Prohibido - La cita pertenece a otro paciente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @Parameter(description = "ID de la cita a buscar", example = "1") 
            @PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }
}
