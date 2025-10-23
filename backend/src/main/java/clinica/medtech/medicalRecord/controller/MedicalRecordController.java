package clinica.medtech.medicalRecord.controller;

import clinica.medtech.medicalRecord.dtoRequest.MedicalRecordRequestDto;
import clinica.medtech.medicalRecord.dtoResponse.MedicalRecordResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.MedicalEntryResponseDto;
import clinica.medtech.medicalRecord.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de historias clínicas.
 */
@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
@Tag(name = "Historias clínicas", description = "Endpoints para la gestión de historias clínicas y sus entradas")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    /**
     * Crea una nueva historia clínica.
     *
     * @param dto DTO con los datos de la historia clínica.
     * @return DTO con la historia clínica creada.
     */
    @Operation(summary = "Crear historia clínica",
            description = "Crea una nueva historia clínica y la persiste en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Historia clínica creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping
    public ResponseEntity<MedicalRecordResponseDto> addMedicalRecord(
            @Parameter(description = "DTO con los datos de la historia clínica") @RequestBody @Valid MedicalRecordRequestDto dto) {
        MedicalRecordResponseDto response = medicalRecordService.addMedicalRecord(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene las entradas médicas asociadas a una historia clínica.
     *
     * @param medicalRecordId ID de la historia clínica.
     * @return Lista de entradas médicas (respuestas simplificadas).
     */
    @Operation(summary = "Listar entradas de una historia clínica",
            description = "Devuelve todas las entradas médicas asociadas a la historia clínica indicada.")
    @ApiResponse(responseCode = "200", description = "Entradas obtenidas correctamente")
    @ApiResponse(responseCode = "404", description = "Historia clínica no encontrada")
    @GetMapping("/{medicalRecordId}/entries")
    public ResponseEntity<List<MedicalEntryResponseDto>> getCompleteMedicalRecord(
            @Parameter(description = "ID de la historia clínica") @PathVariable Long medicalRecordId) {
        List<MedicalEntryResponseDto> entries = medicalRecordService.getCompleteMedicalRecord(medicalRecordId);
        return ResponseEntity.ok(entries);
    }

    /**
     * Obtiene las entradas médicas con detalles (diagnósticos y tratamientos).
     *
     * @param medicalRecordId ID de la historia clínica.
     * @return Lista de entradas médicas con detalles completos.
     */
    @Operation(summary = "Listar entradas con detalles",
            description = "Devuelve las entradas médicas asociadas incluyendo diagnósticos y tratamientos detallados.")
    @ApiResponse(responseCode = "200", description = "Entradas con detalles obtenidas correctamente")
    @ApiResponse(responseCode = "404", description = "Historia clínica no encontrada")
    @GetMapping("/{medicalRecordId}/entries/details")
    public ResponseEntity<List<MedicalEntryResponseDto>> getCompleteMedicalRecordWithDetails(
            @Parameter(description = "ID de la historia clínica") @PathVariable Long medicalRecordId) {
        List<MedicalEntryResponseDto> entries = medicalRecordService.getCompleteMedicalRecordWithDetails(medicalRecordId);
        return ResponseEntity.ok(entries);
    }

    /**
     * Obtiene la historia clínica completa, incluyendo metadatos del MedicalRecord y todas las entradas con sus detalles.
     *
     * @param medicalRecordId ID de la historia clínica.
     * @return DTO con la historia clínica y sus entradas detalladas.
     */
    @Operation(summary = "Obtener historia clínica completa",
            description = "Devuelve la historia clínica con todas sus entradas, diagnósticos y tratamientos.")
    @ApiResponse(responseCode = "200", description = "Historia clínica completa obtenida correctamente")
    @ApiResponse(responseCode = "404", description = "Historia clínica no encontrada")
    @GetMapping("/{medicalRecordId}/full-details")
    public ResponseEntity<MedicalRecordResponseDto> getCompleteMedicalRecordWithDetails2(
            @Parameter(description = "ID de la historia clínica") @PathVariable Long medicalRecordId) {
        MedicalRecordResponseDto response = medicalRecordService.getCompleteMedicalRecordWithDetails2(medicalRecordId);
        return ResponseEntity.ok(response);
    }
}