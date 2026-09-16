package clinica.medtech.medicalRecord.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import clinica.medtech.users.entities.ProfessionalModel;

/* class RegistroMedico {
    - id: String
    - fechaCreacion: DateTime
    - tipo: String
    - resumen: String
    - descripcion: String
    - observaciones: String
    - alergias: String
    - fhirId: String
    + agregarDiagnostico(): void
    + agregarTratamiento(): void
    + sincronizarConEHR(): Boolean
  } */

@Entity
@Table(name = "medical_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalEntryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "summary")
    private String summary;

    @Column(name = "description")
    private String description;

    @Column(name = "observations")
    private String observations;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "fhir_id", unique = true)
    private String fhirId;

    @ManyToOne
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecordModel medicalRecord;

    @OneToMany(mappedBy = "medicalEntry", cascade = CascadeType.ALL)
    private List<TreatmentModel> treatments;

    @OneToMany(mappedBy = "medicalEntry", cascade = CascadeType.ALL)
    private List<DiagnosisModel> diagnoses;

    @ManyToOne
    @JoinColumn(name = "professional_id")
    private ProfessionalModel professional;

}