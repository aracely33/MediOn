package clinica.medtech.medicalRecord.service;

import clinica.medtech.medicalRecord.dtoRequest.MedicalRecordRequestDto;
import clinica.medtech.medicalRecord.entities.MedicalEntryModel;
import clinica.medtech.medicalRecord.entities.MedicalRecordModel;
import clinica.medtech.medicalRecord.mapper.DiagnosisMapper;
import clinica.medtech.medicalRecord.mapper.MedicalEntryMapper;
import clinica.medtech.medicalRecord.mapper.MedicalRecordMapper;
import clinica.medtech.medicalRecord.mapper.TreatmentMapper;
import clinica.medtech.medicalRecord.repository.MedicalEntryRepository;
import clinica.medtech.medicalRecord.repository.MedicalRecordRepository;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import clinica.medtech.medicalRecord.dtoResponse.MedicalRecordResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.TreatmentResponseDto;

import java.util.List;

import clinica.medtech.medicalRecord.dtoResponse.DiagnosisResponseDto;
import clinica.medtech.medicalRecord.dtoResponse.MedicalEntryResponseDto;
import clinica.medtech.medicalRecord.entities.MedicalEntryModel;

/**
 * Servicio para la gestión de historias clínicas.
 */
@Service
@RequiredArgsConstructor
public class MedicalRecordService {

        private final MedicalRecordRepository medicalRecordRepository;

        private final PatientRepository patientRepository;
        private final MedicalEntryMapper medicalEntryMapper;
        private final MedicalRecordMapper medicalRecordMapper;
        private final TreatmentMapper treatmentMapper;
        private final DiagnosisMapper diagnosisMapper;

        /**
         * Agrega una nueva historia clínica.
         */
        public MedicalRecordResponseDto addMedicalRecord(MedicalRecordRequestDto dto) {
                PatientModel patient = patientRepository.findById(dto.getPatientId())
                                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

                MedicalRecordModel medicalRecord = MedicalRecordModel.builder()
                                .number(dto.getNumber())
                                .creationDate(dto.getCreationDate())
                                .observations(dto.getObservations())
                                .patient(patient)
                                .build();

                MedicalRecordModel saved = medicalRecordRepository.save(medicalRecord);

                // Usar el mapper para convertir la entidad a DTO
                return medicalRecordMapper.toResponseDto(saved);
        }

        /**
         * Obtiene el historial completo de entradas médicas (registros médicos) de una
         * historia clínica.
         */
        public List<MedicalEntryResponseDto> getCompleteMedicalRecord(Long medicalRecordId) {
                MedicalRecordModel record = medicalRecordRepository.findById(medicalRecordId)
                                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada"));
                return record.getMedicalEntries().stream()
                                .map(medicalEntryMapper::toResponseDto)
                                .toList();
        }

        /**
         * Obtiene el historial completo de entradas médicas (registros médicos) de una
         * historia clínica,
         * incluyendo tratamientos y diagnósticos completos usando el método
         * toResponseDto2 del mapper.
         */
        public List<MedicalEntryResponseDto> getCompleteMedicalRecordWithDetails(Long medicalRecordId) {
                MedicalRecordModel record = medicalRecordRepository.findById(medicalRecordId)
                                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada"));
                return record.getMedicalEntries().stream()
                                .map(medicalEntryMapper::toResponseDto2)
                                .toList();
        }

        /**
         * Obtiene el historial completo de una historia clínica,
         * incluyendo los datos del MedicalRecord y todas sus entradas médicas con
         * tratamientos y diagnósticos completos.
         */
        public MedicalRecordResponseDto getCompleteMedicalRecordWithDetails2(Long medicalRecordId) {
                MedicalRecordModel record = medicalRecordRepository.findById(medicalRecordId)
                                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada"));

                // Usar el mapper para convertir la entidad a DTO incluyendo las entradas
                // médicas
                MedicalRecordResponseDto dto = medicalRecordMapper.toResponseDto2(record);

                return dto;
        }

}