package clinica.medtech.users.service;

import java.util.List;

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
     * Actualiza los datos de un paciente en la base de datos.
     * Valida que el nuevo email no esté registrado previamente.
     * Actualiza tanto los campos comunes como los específicos de paciente.
     *
     * @param id ID del paciente a actualizar.
     * @param patientUpdateRequest DTO con la información actualizada del paciente.
     * @return DTO de respuesta con los datos actualizados del paciente.
     * @throws EmailAlreadyExistsException si el correo ya está registrado.
     * @throws UsernameNotFoundException si el paciente con el ID especificado no existe.
     */
    @Transactional
    public PatientMeResponseDto updatePatientUser(Long id, PatientUpdateRequestDto patientUpdateRequest) {
        PatientModel patient = patientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Paciente con el id " + id + " no encontrado"));

        if (!patient.getEmail().equals(patientUpdateRequest.getEmail())) {
            patientRepository.findByEmail(patientUpdateRequest.getEmail()).ifPresent(existingUser -> {
                throw new EmailAlreadyExistsException(
                        "El correo " + patientUpdateRequest.getEmail() + " ya existe en la base de datos.");
            });
            patient.setEmail(cleanString(patientUpdateRequest.getEmail()));
        }

        patient.setName(cleanString(patientUpdateRequest.getName()));
        patient.setLastName(cleanString(patientUpdateRequest.getLastName()));
        patient.setGender(cleanString(patientUpdateRequest.getGender()));
        patient.setAddress(cleanString(patientUpdateRequest.getAddress()));
        patient.setPhone(cleanString(patientUpdateRequest.getPhone()));
        patient.setBloodType(cleanString(patientUpdateRequest.getBloodType()));

        patient.setBirthDate(patientUpdateRequest.getBirthDate());

        patientRepository.save(patient);
        return getCurrentPatient(patient.getEmail());
    }

    /**
     * Obtiene los datos del paciente actual por email.
     *
     * @param email Email del paciente.
     * @return DTO con los datos del paciente.
     * @throws UsernameNotFoundException si el paciente con el email especificado no existe.
     */
    public PatientMeResponseDto getCurrentPatient(String email) {
        PatientModel patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Paciente con el email " + email + " no encontrado"));

        return PatientMeResponseDto.builder()
                .id(patient.getId())
                .email(patient.getEmail())
                .name(patient.getName())
                .lastName(patient.getLastName())
                .roles(patient.getRoles().stream()
                        .map(role -> role.getEnumRole().name())
                        .toList())
                .birthDate(patient.getBirthDate())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .phone(patient.getPhone())
                .bloodType(patient.getBloodType())
                .build();
    }

    /**
     * Busca pacientes por nombre.
     *
     * @param name Nombre del paciente.
     * @return Lista de pacientes que coinciden con el nombre.
     */
    public List<PatientModel> findPatientsByName(String name) {
        return patientRepository.findByName(cleanString(name));
    }

    /**
     * Busca pacientes por apellido.
     *
     * @param lastName Apellido del paciente.
     * @return Lista de pacientes que coinciden con el apellido.
     */
    public List<PatientModel> findPatientsByLastName(String lastName) {
        return patientRepository.findByLastName(cleanString(lastName));
    }

    /**
     * Obtiene todos los pacientes registrados.
     *
     * @return Lista de todos los pacientes.
     */
    public List<PatientModel> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Obtiene una página de pacientes, con 20 pacientes por página.
     *
     * @param page Número de página (comienza en 0).
     * @return Página de pacientes.
     */
    public Page<PatientModel> getPatientsPage(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return patientRepository.findAll(pageable);
    }

}
