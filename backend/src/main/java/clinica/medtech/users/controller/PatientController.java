package clinica.medtech.users.controller;

import clinica.medtech.users.dtoRequest.PatientUpdateRequestDto;
import clinica.medtech.users.dtoResponse.PatientMeResponseDto;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /**
     * Actualiza los datos de un paciente.
     *
     * @param id ID del paciente.
     * @param patientUpdateRequest DTO con los datos actualizados.
     * @return Datos actualizados del paciente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientMeResponseDto> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientUpdateRequestDto patientUpdateRequest) {
        PatientMeResponseDto response = patientService.updatePatientUser(id, patientUpdateRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene los datos del paciente actual por email.
     *
     * @param email Email del paciente.
     * @return Datos del paciente.
     */
    @GetMapping("/me")
    public ResponseEntity<PatientMeResponseDto> getCurrentPatient(@RequestParam String email) {
        PatientMeResponseDto response = patientService.getCurrentPatient(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos los pacientes.
     *
     * @return Lista de pacientes.
     */
    @GetMapping
    public ResponseEntity<List<PatientModel>> getAllPatients() {
        List<PatientModel> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    /**
     * Lista pacientes por páginas de 20.
     *
     * @param page Número de página (comienza en 0).
     * @return Página de pacientes.
     */
    @GetMapping("/page")
    public ResponseEntity<Page<PatientModel>> getPatientsPage(@RequestParam(defaultValue = "0") int page) {
        Page<PatientModel> patientsPage = patientService.getPatientsPage(page);
        return ResponseEntity.ok(patientsPage);
    }

    /**
     * Busca pacientes por apellido.
     *
     * @param lastName Apellido del paciente.
     * @return Lista de pacientes que coinciden con el apellido.
     */
    @GetMapping("/search/lastname")
    public ResponseEntity<List<PatientModel>> findPatientsByLastName(@RequestParam String lastName) {
        List<PatientModel> patients = patientService.findPatientsByLastName(lastName);
        return ResponseEntity.ok(patients);
    }

    /**
     * Busca pacientes por nombre.
     *
     * @param name Nombre del paciente.
     * @return Lista de pacientes que coinciden con el nombre.
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<PatientModel>> findPatientsByName(@RequestParam String name) {
        List<PatientModel> patients = patientService.findPatientsByName(name);
        return ResponseEntity.ok(patients);
    }
}