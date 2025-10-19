package clinica.medtech.appointments.controller;

import java.net.URI;
import java.time.LocalDateTime;
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
@Tag(name = "API de Citas Médicas", description = "Operaciones para gestionar las citas de los doctores y pacientes.")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(
        summary = "Agendar una nueva cita médica",
        description = """
            Permite crear una nueva cita médica para un doctor y paciente.
            Se validan posibles conflictos de horario y la duración mínima y máxima permitida.
            """,
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Cita creada exitosamente.",
                content = @Content(schema = @Schema(implementation = AppointmentResponse.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Datos inválidos enviados en la solicitud.",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Conflicto: el doctor ya tiene una cita en ese horario.",
                content = @Content
            )
        }
    )
    @PostMapping
    public ResponseEntity<AppointmentResponse> scheduleAppointment(
            @Valid @RequestBody AppointmentCreateRequest request) {

        AppointmentResponse created = appointmentService.scheduleAppointment(request);
        URI location = URI.create(String.format("/api/appointments/%d", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @Operation(
        summary = "Actualizar parcialmente una cita médica",
        description = """
            Permite modificar algunos datos de una cita existente (PATCH).
            Solo se actualizan los campos enviados en el cuerpo de la solicitud.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos enviados."),
            @ApiResponse(responseCode = "404", description = "No se encontró la cita especificada."),
            @ApiResponse(responseCode = "409", description = "Conflicto de horario con otra cita del mismo doctor.")
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateAppointment(
            @Parameter(description = "ID de la cita que se desea actualizar.", example = "15")
            @PathVariable Long id,
            @Valid @RequestBody AppointmentUpdateRequest request) {

        appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Cancelar una cita médica",
        description = """
            Permite cancelar una cita existente cambiando su estado a CANCELADA.
            Se puede incluir opcionalmente una razón de cancelación.
            """,
        responses = {
            @ApiResponse(responseCode = "204", description = "Cita cancelada correctamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró la cita especificada."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida.")
        }
    )
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(
            @Parameter(description = "ID de la cita que se desea cancelar.", example = "8")
            @PathVariable Long id,
            @Parameter(description = "Motivo opcional de cancelación.", example = "El paciente no podrá asistir por motivos personales.")
            @RequestParam(required = false) String reason) {

        appointmentService.cancelAppointment(id, reason);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Confirmar una cita médica",
        description = """
            Confirma una cita pendiente siempre que no existan conflictos de horario con otras citas del mismo doctor.
            """,
        responses = {
            @ApiResponse(responseCode = "204", description = "Cita confirmada exitosamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró la cita especificada."),
            @ApiResponse(responseCode = "409", description = "Conflicto de horario con otra cita del mismo doctor.")
        }
    )
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmAppointment(
            @Parameter(description = "ID de la cita a confirmar.", example = "6")
            @PathVariable Long id,
            @Parameter(description = "Notas opcionales sobre la confirmación.", example = "Paciente confirmó asistencia.")
            @RequestParam(required = false) String notes) {

        appointmentService.confirmAppointment(id, notes);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener las horas ocupadas de un doctor",
        description = """
            Devuelve una lista con las fechas y horas en las que el doctor ya tiene citas programadas.
            En futuras versiones se ajustará para mostrar únicamente las horas disponibles.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de horarios ocupados obtenida correctamente.",
                content = @Content(schema = @Schema(implementation = LocalDateTime.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No se encontraron citas para el doctor especificado.",
                content = @Content
            )
        }
    )
    @GetMapping("/doctor/{doctorId}/availability")
    public ResponseEntity<List<LocalDateTime>> getDoctorAvailability(
            @Parameter(description = "ID del doctor del cual se desea consultar la disponibilidad.", example = "3")
            @PathVariable Long doctorId) {

        List<LocalDateTime> bookedSlots = appointmentService.getDoctorAvailability(doctorId);
        return ResponseEntity.ok(bookedSlots);
    }
}