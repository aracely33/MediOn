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

/**
 * Controlador REST para la gestión de entradas médicas (MedicalEntry).
 */
@RestController
@RequestMapping("/medical-entries")
@RequiredArgsConstructor
public class MedicalEntryController {

    private final MedicalEntryService medicalEntryService;

    /**
     * Endpoint para agregar una nueva entrada médica.
     */
    @PostMapping
    public ResponseEntity<MedicalEntryResponseDto> addMedicalEntry(@RequestBody MedicalEntryRequestDto dto) {
        MedicalEntryResponseDto response = medicalEntryService.addMedicalEntry(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para agregar un diagnóstico a una entrada médica.
     */
    @PostMapping("/{medicalEntryId}/diagnoses")
    public ResponseEntity<DiagnosisResponseDto> addDiagnosis(
            @PathVariable Long medicalEntryId,
            @RequestBody DiagnosisRequestDto diagnosisDto) {
        DiagnosisResponseDto response = medicalEntryService.addDiagnosis(medicalEntryId, diagnosisDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para agregar un tratamiento a una entrada médica.
     */
    @PostMapping("/{medicalEntryId}/treatments")
    public ResponseEntity<TreatmentResponseDto> addTreatment(
            @PathVariable Long medicalEntryId,
            @RequestBody TreatmentRequestDto treatmentDto) {
        TreatmentResponseDto response = medicalEntryService.addTreatment(medicalEntryId, treatmentDto);
        return ResponseEntity.ok(response);
    }
}