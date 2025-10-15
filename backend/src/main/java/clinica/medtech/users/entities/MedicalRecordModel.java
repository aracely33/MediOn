package clinica.medtech.users.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* 
package "Historia Clínica" {
  
  class HistoriaClinica {
    - id: String
    - numero: String
    - fechaCreacion: DateTime
    - fhirId: String
    + agregarRegistro(): RegistroMedico
    + obtenerHistorialCompleto(): List<RegistroMedico>
  }

 */
/**
 * Entidad que representa la historia clínica de un paciente.
 * 
 * Contiene información relevante como el número de historia clínica, fecha de creación,
 * identificador externo FHIR, observaciones y la relación con el paciente y sus registros médicos.
 * 
 * Relaciones:
 * - Muchas historias clínicas pueden estar asociadas a un paciente.
 * - Una historia clínica puede tener múltiples registros médicos.
 * 
 * Campos principales:
 * - id: Identificador único de la historia clínica.
 * - number: Número de historia clínica asignado por el centro de salud.
 * - creationDate: Fecha de creación de la historia clínica.
 * - fhirId: Identificador externo para interoperabilidad con sistemas FHIR.
 * - patient: Paciente al que pertenece la historia clínica.
 * - registrosMedicos: Lista de registros médicos asociados a la historia clínica.
 * - observaciones: Notas y observaciones adicionales.
 */

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medical_records")
public class MedicalRecordModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "fhir_id", unique = true)
    private String fhirId;

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientModel patient;

    @OneToMany(mappedBy = "medical_record", cascade = CascadeType.ALL)
    private List<MedicalEntryModel> medicalEntries;

    @Column(name = "observations")
    private String observations;

}
