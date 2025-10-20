package clinica.medtech.medicalRecord.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

/**
 * Entidad que representa un diagnóstico médico asociado a una entrada médica.
 */
@Entity
@Table(name = "diagnoses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único del diagnóstico

    @Column(name = "code", nullable = false)
    private String code; // Código del diagnóstico (por ejemplo, CIE-10)

    @Column(name = "code_system")
    private String codeSystem; // Sistema de codificación utilizado

    @Column(name = "description")
    private String description; // Descripción del diagnóstico

    @Column(name = "start_date")
    private Date startDate; // Fecha de inicio del diagnóstico

    @Column(name = "severity")
    private String severity; // Severidad del diagnóstico

    @Column(name = "fhir_id", unique = true)
    private String fhirId; // Identificador único en el sistema FHIR

    @ManyToOne
    @JoinColumn(name = "medical_entry_id", nullable = false)
    private MedicalEntryModel medicalEntry; // Entrada médica a la que pertenece este diagnóstico

}