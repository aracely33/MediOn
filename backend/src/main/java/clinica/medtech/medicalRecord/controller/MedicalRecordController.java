package clinica.medtech.medicalRecord.controller;

import clinica.medtech.medicalRecord.dtoRequest.MedicalRecordRequestDto;
import clinica.medtech.medicalRecord.dtoResponse.MedicalRecordResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.MedicalEntryResponseDto;
import clinica.medtech.medicalRecord.service.MedicalRecordService;
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
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    /**
     * Endpoint para agregar una nueva historia clínica.
     */
    @PostMapping
    public ResponseEntity<MedicalRecordResponseDto> addMedicalRecord(@RequestBody MedicalRecordRequestDto dto) {
        MedicalRecordResponseDto response = medicalRecordService.addMedicalRecord(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obtener el historial completo de entradas médicas de una historia clínica.
     */
    @GetMapping("/{medicalRecordId}/entries")
    public ResponseEntity<List<MedicalEntryResponseDto>> getCompleteMedicalRecord(@PathVariable Long medicalRecordId) {
        List<MedicalEntryResponseDto> entries = medicalRecordService.getCompleteMedicalRecord(medicalRecordId);
        return ResponseEntity.ok(entries);
    }
    /**
     * Endpoint para obtener el historial completo de entradas médicas de una historia clínica,
     * incluyendo tratamientos y diagnósticos completos.
     */
    @GetMapping("/{medicalRecordId}/entries/details")
    public ResponseEntity<List<MedicalEntryResponseDto>> getCompleteMedicalRecordWithDetails(@PathVariable Long medicalRecordId) {
        List<MedicalEntryResponseDto> entries = medicalRecordService.getCompleteMedicalRecordWithDetails(medicalRecordId);
        return ResponseEntity.ok(entries);
    }

        /**
     * Endpoint para obtener el historial completo de una historia clínica,
     * incluyendo los datos del MedicalRecord y todas sus entradas médicas con tratamientos y diagnósticos completos.
     */
    @GetMapping("/{medicalRecordId}/full-details")
    public ResponseEntity<MedicalRecordResponseDto> getCompleteMedicalRecordWithDetails2(@PathVariable Long medicalRecordId) {
        MedicalRecordResponseDto response = medicalRecordService.getCompleteMedicalRecordWithDetails2(medicalRecordId);
        return ResponseEntity.ok(response);
    }
}