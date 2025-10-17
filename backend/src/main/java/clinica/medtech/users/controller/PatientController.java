package clinica.medtech.users.controller;

import clinica.medtech.common.dto.PaginatedResponse;
import clinica.medtech.users.dtoRequest.PatientUpdateRequestDto;
import clinica.medtech.users.dtoResponse.PatientMeResponseDto;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Endpoints para la gestión de los pacientes de la clínica")
public class PatientController {

    private final PatientService patientService;

    /**
     * Actualiza los datos de un paciente.
     *
     * @param id ID del paciente.
     * @param updateRequest Datos actualizados del paciente.
     * @return Datos actualizados del paciente.
     */
    @Operation(summary = "Actualizar datos de un paciente",
            description = "Permite modificar los datos personales de un paciente existente.")
    @ApiResponse(responseCode = "200", description = "Paciente actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o correo duplicado")
    @PatchMapping("/{id}")
    public ResponseEntity<PatientMeResponseDto> updatePatient(
            @Parameter(description = "ID del paciente a actualizar") @PathVariable Long id,
            @RequestBody @Valid PatientUpdateRequestDto updateRequest) {
        return ResponseEntity.ok(patientService.updatePatientUser(id, updateRequest));
    }


    @Operation(summary = "Listar todos los pacientes",
            description = "Obtiene una lista paginada de todos los pacientes registrados.")
    @GetMapping
    public ResponseEntity<PaginatedResponse<PatientMeResponseDto>> getAllPatients(
            @Parameter(description = "Número de página (inicia en 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(patientService.getAllPatients(page, size));
    }

    @Operation(summary = "Obtener paciente por ID",
            description = "Devuelve la información de un paciente por su ID único.")
    @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<PatientMeResponseDto> getPatientById(
            @Parameter(description = "ID del paciente") @PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    @Operation(summary = "Buscar paciente por email",
            description = "Devuelve un paciente según su dirección de correo electrónico.")
    @GetMapping("/email/{email}")
    public ResponseEntity<PatientMeResponseDto> getByEmail(
            @Parameter(description = "Correo electrónico del paciente") @PathVariable String email) {
        return ResponseEntity.ok(patientService.getByEmail(email));
    }

    @Operation(summary = "Buscar pacientes por nombre",
            description = "Busca pacientes cuyo nombre contenga la cadena proporcionada (no sensible a mayúsculas).")
    @GetMapping("/search/name")
    public ResponseEntity<List<PatientMeResponseDto>> searchByName(
            @Parameter(description = "Nombre o parte del nombre del paciente") @RequestParam String name) {
        return ResponseEntity.ok(patientService.searchByName(name));
    }

    @Operation(summary = "Buscar pacientes por apellido",
            description = "Busca pacientes cuyo apellido contenga la cadena proporcionada (no sensible a mayúsculas).")
    @GetMapping("/search/lastname")
    public ResponseEntity<List<PatientMeResponseDto>> searchByLastName(
            @Parameter(description = "Apellido o parte del apellido del paciente") @RequestParam String lastName) {
        return ResponseEntity.ok(patientService.searchByLastName(lastName));
    }

    @Operation(summary = "Listar pacientes por grupo sanguíneo",
            description = "Obtiene una lista paginada de pacientes según su tipo de sangre.")
    @GetMapping("/bloodtype")
    public ResponseEntity<PaginatedResponse<PatientMeResponseDto>> getByBloodType(
            @Parameter(description = "Tipo de sangre a buscar") @RequestParam String bloodType,
            @Parameter(description = "Número de página (inicia en 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(patientService.getByBloodType(bloodType, page, size));
    }
}