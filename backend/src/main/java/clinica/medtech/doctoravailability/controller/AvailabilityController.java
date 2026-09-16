package clinica.medtech.doctoravailability.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import clinica.medtech.doctoravailability.dtos.request.CreateAvailabilityDto;
import clinica.medtech.doctoravailability.dtos.request.UpdateAvailabilityDto;
import clinica.medtech.doctoravailability.dtos.response.AvailabilityResponseDto;
import clinica.medtech.doctoravailability.service.AvailabilityService;
import clinica.medtech.exceptions.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
@Validated
@Tag(
    name = "Doctor Availability API",
    description = "Gestión de disponibilidades médicas: creación, actualización parcial (PATCH), desactivación, y consulta."
)
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @Operation(
        summary = "Crear disponibilidad médica",
        description = """
            Crea una nueva franja horaria disponible para un médico.
            Todos los campos son obligatorios y deben respetar el formato de hora (HH:mm).
            """,
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Disponibilidad creada correctamente.",
                content = @Content(schema = @Schema(implementation = AvailabilityResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Datos inválidos o formato de hora incorrecto.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Conflicto: ya existe una disponibilidad en el mismo horario para el médico.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    @PostMapping
    public ResponseEntity<AvailabilityResponseDto> create(@Valid @RequestBody CreateAvailabilityDto dto) {
        AvailabilityResponseDto created = availabilityService.createAvailability(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(
        summary = "Actualizar parcialmente una disponibilidad médica",
        description = """
            Permite actualizar **uno o varios** campos de una disponibilidad existente.
            Solo los campos enviados serán modificados.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Disponibilidad actualizada correctamente.",
                content = @Content(schema = @Schema(implementation = AvailabilityResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Datos inválidos (por ejemplo, hora de inicio mayor que hora de fin).",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No se encontró la disponibilidad médica especificada.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAvailabilityDto dto) {

        AvailabilityResponseDto updated = availabilityService.updateAvailability(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Desactivar disponibilidad médica",
        description = """
            Desactiva una disponibilidad sin eliminarla de la base de datos.
            Esto permite mantener el historial, pero el médico no estará disponible en ese horario.
            """,
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Disponibilidad desactivada correctamente (sin contenido en la respuesta)."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No se encontró la disponibilidad a desactivar.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        availabilityService.deactivateAvailability(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Consultar una disponibilidad médica por ID",
        description = """
            Devuelve la información completa de una disponibilidad específica.
            Incluye día de la semana, horario y estado (activo/inactivo).
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Disponibilidad encontrada.",
                content = @Content(schema = @Schema(implementation = AvailabilityResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No existe una disponibilidad con el ID especificado.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(availabilityService.getAvailability(id));
    }

    @Operation(
        summary = "Listar disponibilidades activas por médico",
        description = """
            Retorna todas las disponibilidades activas registradas por un médico específico.
            Se puede usar para mostrar su agenda semanal o validaciones antes de agendar citas.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista obtenida correctamente.",
                content = @Content(schema = @Schema(implementation = AvailabilityResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "El médico no tiene disponibilidades activas registradas.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AvailabilityResponseDto>> listByDoctor(@PathVariable Long doctorId) {
        List<AvailabilityResponseDto> list = availabilityService.listByDoctor(doctorId);
        return ResponseEntity.ok(list);
    }
}