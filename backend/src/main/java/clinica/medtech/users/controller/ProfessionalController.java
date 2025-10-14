package clinica.medtech.users.controller;

import clinica.medtech.common.dto.PaginatedResponse;
import clinica.medtech.users.dtoResponse.ProfessionalResponseDto;
import clinica.medtech.users.service.ProfessionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professional")
@RequiredArgsConstructor
@Tag(name = "Profesionales", description = "Endpoints para gestión y búsqueda de profesionales médicos")
public class ProfessionalController {
    private final ProfessionalService professionalService;

    // Obtener todos los profesionales (paginado)
    @GetMapping
    @Operation(summary = "Obtener todos los profesionales", description = "Devuelve una lista paginada de todos los profesionales registrados.")
    public PaginatedResponse<ProfessionalResponseDto> getAllProfessionals(
            @Parameter(description = "Número de página (comienza en 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        return professionalService.getAllProfessionals(page, size);
    }

    // Obtener profesional por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener profesional por ID", description = "Devuelve los datos de un profesional específico según su ID.")
    public ProfessionalResponseDto getProfessionalById(
            @Parameter(description = "ID del profesional") @PathVariable Long id
    ) {
        return professionalService.getProfessional(id);
    }

    // Obtener profesional por matrícula (única)
    @GetMapping("/license/{license}")
    @Operation(summary = "Obtener profesional por matrícula", description = "Devuelve un profesional mediante su número de matrícula profesional.")
    public ProfessionalResponseDto getByMedicalLicense(
            @Parameter(description = "Número de matrícula profesional") @PathVariable String license
    ) {
        return professionalService.getByMedicalLicense(license);
    }

    // Obtener profesional por email (único)
    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener profesional por email", description = "Devuelve un profesional según su dirección de correo electrónico.")
    public ProfessionalResponseDto getByEmail(
            @Parameter(description = "Correo electrónico del profesional") @PathVariable String email
    ) {
        return professionalService.getByEmail(email);
    }

    // Buscar profesionales por nombre (coincidencia parcial)
    @GetMapping("/search")
    @Operation(summary = "Buscar profesionales por nombre", description = "Busca profesionales cuyo nombre contenga el texto proporcionado (sin distinción de mayúsculas).")
    public List<ProfessionalResponseDto> searchByName(
            @Parameter(description = "Nombre o parte del nombre a buscar") @RequestParam String name
    ) {
        return professionalService.searchByName(name);
    }

    // Listar por especialidad con paginación
    @GetMapping("/specialty")
    @Operation(summary = "Buscar profesionales por especialidad", description = "Devuelve una lista paginada de profesionales que coincidan con la especialidad indicada.")
    public PaginatedResponse<ProfessionalResponseDto> getBySpecialty(
            @Parameter(description = "Nombre de la especialidad (ej: Cardiología)") @RequestParam String specialty,
            @Parameter(description = "Número de página (comienza en 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        return professionalService.getBySpecialty(specialty, page, size);
    }
}
