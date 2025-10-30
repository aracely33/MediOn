package clinica.medtech.users.controller;

import clinica.medtech.common.dto.PaginatedResponse;
import clinica.medtech.users.dtoRequest.PatientUpdateRequestDto;
import clinica.medtech.users.dtoResponse.PatientMeResponseDto;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.service.FhirPatientService;
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
    private final FhirPatientService fhirPatientService;

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

    @GetMapping("/fhir/search")
    public ResponseEntity<String> getPatientByEmail(@RequestParam String email) {
        try {
            String response = fhirPatientService.getPatientByEmail(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"No se pudo obtener el paciente desde FHIR\"}");
        }
    }

    @Operation(summary = "Buscar paciente por ID FHIR",
            description = "Obtiene un paciente del servidor FHIR utilizando su ID FHIR único.")
    @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Paciente no encontrado en el servidor FHIR")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @GetMapping("/fhir/{fhirId}")
    public ResponseEntity<String> getPatientByFhirId(
            @Parameter(description = "ID FHIR del paciente") @PathVariable String fhirId) {
        try {
            String response = fhirPatientService.getPatientByIDFhir(fhirId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"No se pudo obtener el paciente desde FHIR con ID: " + fhirId + "\"}");
        }
    }

    @Operation(summary = "Crear paciente en FHIR",
            description = "Crea un nuevo paciente en el servidor FHIR utilizando los datos del paciente local. " +
                    "El paciente debe existir previamente en la base de datos local.")
    @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente en FHIR")
    @ApiResponse(responseCode = "404", description = "Paciente no encontrado en la base de datos local")
    @ApiResponse(responseCode = "500", description = "Error al crear el paciente en FHIR")
    @PostMapping("/{patientId}/fhir")
    public ResponseEntity<String> createPatientInFhir(
            @Parameter(description = "ID del paciente local") @PathVariable Long patientId) {
        try {
            // Obtener la entidad PatientModel de la base de datos local
            PatientModel patient = patientService.getPatientEntity(patientId);
            
            // Crear el paciente en FHIR usando el método básico
            String response = fhirPatientService.createPatientOnFhir(patient);
            
            return ResponseEntity.status(201).body(response);
                    
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"Error al crear el paciente en FHIR: " + e.getMessage() + "\"}");
        }
    }

    @Operation(summary = "Crear paciente completo en FHIR",
            description = "Crea un paciente en FHIR usando el mapper completo con todos los campos disponibles. " +
                    "Utiliza la estructura FHIR estándar con todos los datos del paciente.")
    @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente con estructura completa")
    @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    @ApiResponse(responseCode = "500", description = "Error al crear el paciente en FHIR")
    @PostMapping("/{patientId}/fhir/complete")
    public ResponseEntity<String> createCompletePatientInFhir(
            @Parameter(description = "ID del paciente local") @PathVariable Long patientId) {
        try {
            // Obtener la entidad PatientModel de la base de datos local
            PatientModel patient = patientService.getPatientEntity(patientId);
            
            // Crear el paciente en FHIR usando el método completo
            String response = fhirPatientService.createCompleteFhirPatient(patient);
            
            return ResponseEntity.status(201).body(response);
                    
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"Error al crear el paciente completo en FHIR: " + e.getMessage() + "\"}");
        }
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