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
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@Tag(
    name = "API de Citas Médicas",
    description = "Operaciones para gestionar las citas médicas de doctores y pacientes."
)
public class AppointmentController {

    private final AppointmentService appointmentService;

    //1. Crear cita médica
    @Operation(
        summary = "Agendar una nueva cita médica",
        description = """
            Permite crear una nueva cita médica para un doctor y un paciente.
            Se validan conflictos de horario, tipo de cita (presencial o virtual)
            y duración mínima y máxima permitida.
            """,
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Cita creada exitosamente.",
                content = @Content(schema = @Schema(implementation = AppointmentResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos enviados en la solicitud."),
            @ApiResponse(responseCode = "409", description = "Conflicto: el doctor ya tiene otra cita en ese horario.")
        }
    )
    @PostMapping
    public ResponseEntity<AppointmentResponse> scheduleAppointment(
            @Valid @RequestBody AppointmentCreateRequest request) {

        AppointmentResponse created = appointmentService.scheduleAppointment(request);
        URI location = URI.create(String.format("/api/appointments/%d", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    //2. Actualizar parcialmente una cita médica
    @Operation(
        summary = "Actualizar parcialmente una cita médica",
        description = """
            Permite modificar algunos datos de una cita existente (PATCH).
            Solo se actualizan los campos enviados en la solicitud.
            Si se cambia la fecha u hora, se valida que no exista conflicto.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos enviados."),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada."),
            @ApiResponse(responseCode = "409", description = "Conflicto de horario con otra cita.")
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateAppointment(
            @Parameter(description = "ID de la cita a actualizar.", example = "15")
            @PathVariable Long id,
            @Valid @RequestBody AppointmentUpdateRequest request) {

        appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok().build();
    }

    //3. Cancelar cita médica
    @Operation(
        summary = "Cancelar una cita médica",
        description = """
            Permite cancelar una cita existente cambiando su estado a CANCELADA.
            Se puede incluir opcionalmente un motivo de cancelación. 
            Las citas canceladas dejan de aparecer como horarios ocupados.
            """,
        responses = {
            @ApiResponse(responseCode = "204", description = "Cita cancelada correctamente."),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada."),
            @ApiResponse(responseCode = "400", description = "La cita ya estaba cancelada.")
        }
    )
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(
            @Parameter(description = "ID de la cita a cancelar.", example = "8")
            @PathVariable Long id,
            @Parameter(description = "Motivo opcional de cancelación.", example = "El paciente no podrá asistir.")
            @RequestParam(required = false) String reason) {

        appointmentService.cancelAppointment(id, reason);
        return ResponseEntity.noContent().build();
    }

    //4. Confirmar cita médica
    @Operation(
        summary = "Confirmar una cita médica",
        description = """
            Confirma una cita pendiente siempre que no existan conflictos
            de horario con otras citas del mismo doctor.
            """,
        responses = {
            @ApiResponse(responseCode = "204", description = "Cita confirmada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada."),
            @ApiResponse(responseCode = "409", description = "Conflicto de horario con otra cita.")
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

    //5. Consultar disponibilidad del doctor
    @Operation(
        summary = "Obtener las horas ocupadas de un doctor",
        description = """
            Devuelve una lista con las fechas y horas en las que el doctor ya tiene citas programadas.
            Solo se incluyen citas pendientes, confirmadas o en curso (no canceladas).
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de horarios ocupados obtenida correctamente.",
                content = @Content(schema = @Schema(implementation = LocalDateTime.class))
            ),
            @ApiResponse(responseCode = "404", description = "No se encontraron citas activas para el doctor especificado.")
        }
    )
    @GetMapping("/doctor/{doctorId}/availability")
    public ResponseEntity<List<LocalDateTime>> getDoctorAvailability(
            @Parameter(description = "ID del doctor del cual se desea consultar la disponibilidad.", example = "3")
            @PathVariable Long doctorId) {

        List<LocalDateTime> bookedSlots = appointmentService.getDoctorAvailability(doctorId);
        return ResponseEntity.ok(bookedSlots);
    }

    @Operation(
    summary = "Obtener todas las citas de un paciente",
    description = """
        Devuelve una lista con todas las citas registradas para un paciente específico,
        ordenadas por fecha y hora.
        """,
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de citas obtenida correctamente.",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppointmentResponse.class)))
        ),
        @ApiResponse(responseCode = "404", description = "No se encontraron citas para el paciente indicado.")
    }
    )
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPatientId(
            @Parameter(description = "ID del paciente del cual se desean obtener las citas.", example = "42")
            @PathVariable Long patientId) {

        List<AppointmentResponse> responses = appointmentService.getAppointmentsByPatientId(patientId);
        return ResponseEntity.ok(responses);
    }
}