package clinica.medtech.users.service;

import java.util.List;

import clinica.medtech.common.dto.PaginatedResponse;
import clinica.medtech.exceptions.PatientNotFoundException;
import clinica.medtech.users.mapper.PatientMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clinica.medtech.exceptions.EmailAlreadyExistsException;
import clinica.medtech.users.dtoRequest.PatientUpdateRequestDto;
import clinica.medtech.users.dtoResponse.PatientMeResponseDto;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.entities.UserModel;
import clinica.medtech.users.repository.PatientRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Servicio para la gestión de pacientes.
 * Permite actualizar, consultar y listar pacientes, así como filtrar por nombre y apellido.
 */
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    /**
     * Limpia una cadena eliminando espacios extra y normalizando el formato.
     *
     * @param value Cadena a limpiar.
     * @return Cadena limpia o null si el valor es null.
     */
    private String cleanString(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }


    /**
     * Actualiza los datos de un paciente existente en la base de datos.
     * Valida que el nuevo correo no esté registrado por otro usuario.
     *
     * @param id ID del paciente a actualizar.
     * @param updateRequest DTO con los datos nuevos.
     * @return DTO con la información actualizada.
     */
    @Transactional
    public PatientMeResponseDto updatePatientUser(Long id, PatientUpdateRequestDto updateRequest) {
        PatientModel patient = patientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Paciente con id " + id + " no encontrado."));

        // Actualizar campos comunes
        patient.setName(cleanString(updateRequest.getName()));
        patient.setLastName(cleanString(updateRequest.getLastName()));
        patient.setGender(cleanString(updateRequest.getGender()));
        patient.setAddress(cleanString(updateRequest.getAddress()));
        patient.setPhone(cleanString(updateRequest.getPhone()));
        patient.setBloodType(cleanString(updateRequest.getBloodType()));
        patient.setBirthDate(updateRequest.getBirthDate());
        patient.setCity(updateRequest.getCity());
        patient.setCountry(updateRequest.getCountry());
        patient.setZip(updateRequest.getZip());

        PatientModel updated = patientRepository.save(patient);

        return patientMapper.mapToDto(updated);
    }
    /**
     * Obtener todos los pacientes de forma paginada.
     */
    public PaginatedResponse<PatientMeResponseDto> getAllPatients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());
        Page<PatientModel> patients = patientRepository.findAll(pageable);

        List<PatientMeResponseDto> content = patientMapper.mapToDtoList(patients.getContent());

        return new PaginatedResponse<>(
                content,
                patients.getNumber(),
                patients.getSize(),
                patients.getTotalPages(),
                patients.getTotalElements()
        );
    }

    /**
     * Obtener paciente por ID.
     */
    public PatientMeResponseDto getPatient(Long id) {
        PatientModel patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente con id " + id + " no encontrado"));
        return patientMapper.mapToDto(patient);
    }

    /**
     * Obtener paciente por email.
     */
    public PatientMeResponseDto getByEmail(String email) {
        PatientModel patient = patientRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new PatientNotFoundException("Paciente con email " + email + " no encontrado"));
        return patientMapper.mapToDto(patient);
    }

    /**
     * Buscar pacientes por nombre (coincidencia parcial, sin mayúsculas/minúsculas).
     */
    public List<PatientMeResponseDto> searchByName(String name) {
        List<PatientModel> patients = patientRepository.findByNameContainingIgnoreCase(name);

        if (patients.isEmpty()) {
            throw new PatientNotFoundException("No se encontraron pacientes con el nombre: " + name);
        }

        return patientMapper.mapToDtoList(patients);
    }

    /**
     * Buscar pacientes por apellido (coincidencia parcial, sin mayúsculas/minúsculas).
     */
    public List<PatientMeResponseDto> searchByLastName(String lastName) {
        List<PatientModel> patients = patientRepository.findByLastNameContainingIgnoreCase(lastName);

        if (patients.isEmpty()) {
            throw new PatientNotFoundException("No se encontraron pacientes con el apellido: " + lastName);
        }

        return patientMapper.mapToDtoList(patients);
    }

    /**
     * Obtener pacientes por grupo sanguíneo con paginación.
     */
    public PaginatedResponse<PatientMeResponseDto> getByBloodType(String bloodType, int page, int size) {
        Page<PatientModel> patients = patientRepository.findByBloodTypeContainingIgnoreCase(
                bloodType, PageRequest.of(page, size)
        );

        List<PatientMeResponseDto> content = patientMapper.mapToDtoList(patients.getContent());

        return new PaginatedResponse<>(
                content,
                patients.getNumber(),
                patients.getSize(),
                patients.getTotalPages(),
                patients.getTotalElements()
        );
    }

}
