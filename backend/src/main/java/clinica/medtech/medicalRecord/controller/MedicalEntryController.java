// ...existing code...
package clinica.medtech.medicalRecord.controller;

import clinica.medtech.medicalRecord.dtoRequest.DiagnosisRequestDto;
import clinica.medtech.medicalRecord.dtoRequest.MedicalEntryRequestDto;
import clinica.medtech.medicalRecord.dtoRequest.TreatmentRequestDto;
import clinica.medtech.medicalRecord.dtoResponse.DiagnosisResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.MedicalEntryResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.TreatmentResponseDto;
import clinica.medtech.medicalRecord.service.MedicalEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la gestión de entradas médicas (MedicalEntry).
 */
@RestController
@RequestMapping("/medical-entries")
@RequiredArgsConstructor
@Tag(name = "Entradas médicas", description = "Endpoints para la gestión de entradas médicas (medical entries)")
public class MedicalEntryController {

    private final MedicalEntryService medicalEntryService;

    /**
     * Agrega una nueva entrada médica.
     *
     * @param dto DTO con los datos de la entrada médica.
     * @return Entrada médica creada.
     */
    @Operation(summary = "Crear entrada médica",
            description = "Crea una nueva entrada médica asociada a una historia clínica y profesional.")
    @ApiResponse(responseCode = "200", description = "Entrada médica creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping
    public ResponseEntity<MedicalEntryResponseDto> addMedicalEntry(
            @RequestBody @Valid MedicalEntryRequestDto dto) {
        MedicalEntryResponseDto response = medicalEntryService.addMedicalEntry(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Agrega un diagnóstico a una entrada médica existente.
     *
     * @param medicalEntryId ID de la entrada médica.
     * @param diagnosisDto   DTO con los datos del diagnóstico.
     * @return Diagnóstico creado.
     */
    @Operation(summary = "Agregar diagnóstico a una entrada médica",
            description = "Agrega un diagnóstico (diagnosis) a la entrada médica indicada por su ID.")
    @ApiResponse(responseCode = "200", description = "Diagnóstico agregado correctamente")
    @ApiResponse(responseCode = "404", description = "Entrada médica no encontrada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping("/{medicalEntryId}/diagnoses")
    public ResponseEntity<DiagnosisResponseDto> addDiagnosis(
            @Parameter(description = "ID de la entrada médica") @PathVariable Long medicalEntryId,
            @RequestBody @Valid DiagnosisRequestDto diagnosisDto) {
        DiagnosisResponseDto response = medicalEntryService.addDiagnosis(medicalEntryId, diagnosisDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Agrega un tratamiento a una entrada médica existente.
     *
     * @param medicalEntryId ID de la entrada médica.
     * @param treatmentDto   DTO con los datos del tratamiento.
     * @return Tratamiento creado.
     */
    @Operation(summary = "Agregar tratamiento a una entrada médica",
            description = "Agrega un tratamiento (treatment) a la entrada médica indicada por su ID.")
    @ApiResponse(responseCode = "200", description = "Tratamiento agregado correctamente")
    @ApiResponse(responseCode = "404", description = "Entrada médica no encontrada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping("/{medicalEntryId}/treatments")
    public ResponseEntity<TreatmentResponseDto> addTreatment(
            @Parameter(description = "ID de la entrada médica") @PathVariable Long medicalEntryId,
            @RequestBody @Valid TreatmentRequestDto treatmentDto) {
        TreatmentResponseDto response = medicalEntryService.addTreatment(medicalEntryId, treatmentDto);
        return ResponseEntity.ok(response);
    }
}