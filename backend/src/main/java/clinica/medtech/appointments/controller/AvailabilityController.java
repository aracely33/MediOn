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
    name = "Availabilities API",
    description = "Gestión de disponibilidades médicas (crear, actualizar, desactivar, consultar)."
)
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @Operation(
        summary = "Crear disponibilidad de un médico",
        description = "Registra una nueva franja horaria disponible para un médico.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Disponibilidad creada exitosamente",
                content = @Content(schema = @Schema(implementation = AvailabilityResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
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
        summary = "Actualizar disponibilidad de un médico",
        description = "Permite modificar la información de una disponibilidad existente.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Disponibilidad actualizada correctamente",
                content = @Content(schema = @Schema(implementation = AvailabilityResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada", content = @Content)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateAvailabilityDto dto) {
        AvailabilityResponseDto updated = availabilityService.updateAvailability(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Desactivar disponibilidad",
        description = "Marca una disponibilidad como inactiva sin eliminarla de la base de datos.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Disponibilidad desactivada correctamente"),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        availabilityService.deactivateAvailability(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener disponibilidad por ID",
        description = "Devuelve los detalles de una disponibilidad específica.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Disponibilidad encontrada",
                content = @Content(schema = @Schema(implementation = AvailabilityResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDto> get(@PathVariable Long id) {
        AvailabilityResponseDto dto = availabilityService.getAvailability(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "Listar disponibilidades de un médico",
        description = "Obtiene todas las disponibilidades activas asociadas a un médico.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de disponibilidades obtenida correctamente",
                content = @Content(schema = @Schema(implementation = AvailabilityResponseDto.class))
            )
        }
    )
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AvailabilityResponseDto>> listByDoctor(@PathVariable Long doctorId) {
        List<AvailabilityResponseDto> list = availabilityService.listByDoctor(doctorId);
        return ResponseEntity.ok(list);
    }
}
