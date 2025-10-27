package clinica.medtech.doctoravailability.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import clinica.medtech.doctoravailability.service.DoctorAvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
@Tag(
    name = "Disponibilidad de Doctores",
    description = "Permite consultar las horas disponibles de un doctor para agendar citas médicas, según su horario configurado."
)
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;

    @Operation(
        summary = "Consultar disponibilidad por fecha",
        description = """
            Devuelve una lista de horas disponibles para agendar una cita en una fecha específica con el doctor seleccionado.
            Si el doctor no tiene horario ese día o todas las horas están ocupadas, se devuelve una lista vacía.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disponibilidad consultada correctamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos (fecha o ID de doctor incorrectos)"),
        @ApiResponse(responseCode = "404", description = "No se encontró el doctor solicitado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<LocalTime>> getDoctorAvailability(
            @Parameter(
                description = "ID del doctor del cual se desea consultar la disponibilidad",
                example = "7",
                required = true
            )
            @PathVariable Long doctorId,

            @Parameter(
                description = "Fecha para consultar las horas disponibles (formato: yyyy-MM-dd)",
                example = "2025-11-18",
                required = true
            )
            @RequestParam LocalDate date) {

        List<LocalTime> availableSlots = availabilityService.getAvailableSlots(doctorId, date);
        return ResponseEntity.ok(availableSlots);
    }
}