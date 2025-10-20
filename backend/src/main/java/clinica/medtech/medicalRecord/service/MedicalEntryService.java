package clinica.medtech.medicalRecord.service;

import clinica.medtech.medicalRecord.dtoRequest.DiagnosisRequestDto;
import clinica.medtech.medicalRecord.dtoRequest.MedicalEntryRequestDto;
import clinica.medtech.medicalRecord.dtoRequest.TreatmentRequestDto;
import clinica.medtech.medicalRecord.dtoResponse.DiagnosisResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.MedicalEntryResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.TreatmentResponseDto;
import clinica.medtech.medicalRecord.entities.DiagnosisModel;
import clinica.medtech.medicalRecord.entities.MedicalEntryModel;
import clinica.medtech.medicalRecord.entities.MedicalRecordModel;
import clinica.medtech.medicalRecord.entities.TreatmentModel;
import clinica.medtech.medicalRecord.mapper.DiagnosisMapper;
import clinica.medtech.medicalRecord.mapper.MedicalEntryMapper;
import clinica.medtech.medicalRecord.mapper.TreatmentMapper;
import clinica.medtech.medicalRecord.repository.DiagnosisRepository;
import clinica.medtech.medicalRecord.repository.MedicalEntryRepository;
import clinica.medtech.medicalRecord.repository.MedicalRecordRepository;
import clinica.medtech.medicalRecord.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de entradas médicas (MedicalEntry).
 */
@Service
@RequiredArgsConstructor
public class MedicalEntryService {

    private final MedicalEntryRepository medicalEntryRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final TreatmentRepository treatmentRepository;
    private final DiagnosisMapper diagnosisMapper;
    private final TreatmentMapper treatmentMapper;
    private final MedicalEntryMapper medicalEntryMapper;

    /**
     * Agrega un diagnóstico a una entrada médica.
     */
    public DiagnosisResponseDto addDiagnosis(Long medicalEntryId, DiagnosisRequestDto diagnosisDto) {
        MedicalEntryModel entry = medicalEntryRepository.findById(medicalEntryId)
                .orElseThrow(() -> new IllegalArgumentException("Entrada médica no encontrada"));
        DiagnosisModel diagnosis = diagnosisMapper.toEntity(diagnosisDto);
        diagnosis.setMedicalEntry(entry);
        DiagnosisModel saved = diagnosisRepository.save(diagnosis);
        return diagnosisMapper.toResponseDto(saved);
    }

    /**
     * Agrega un tratamiento a una entrada médica.
     */
    public TreatmentResponseDto addTreatment(Long medicalEntryId, TreatmentRequestDto treatmentDto) {
        MedicalEntryModel entry = medicalEntryRepository.findById(medicalEntryId)
                .orElseThrow(() -> new IllegalArgumentException("Entrada médica no encontrada"));
        TreatmentModel treatment = treatmentMapper.toEntity(treatmentDto);
        treatment.setMedicalEntry(entry);
        TreatmentModel saved = treatmentRepository.save(treatment);
        return treatmentMapper.toResponseDto(saved);
    }
    /**
     * Agrega una nueva entrada médica y devuelve el DTO de respuesta.
     */
    public MedicalEntryResponseDto addMedicalEntry(MedicalEntryRequestDto dto) {
        MedicalRecordModel medicalRecord = medicalRecordRepository.findById(dto.getMedicalRecordId())
                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada"));

        MedicalEntryModel entry = medicalEntryMapper.toEntity(dto);
        entry.setMedicalRecord(medicalRecord);

        MedicalEntryModel saved = medicalEntryRepository.save(entry);

        return medicalEntryMapper.toResponseDto(saved);
    }
}