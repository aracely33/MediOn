package clinica.medtech.users.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

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
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "code_system")
    private String codeSystem;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "severity")
    private String severity;

    @Column(name = "fhir_id", unique = true)
    private String fhirId;

    @ManyToOne
    @JoinColumn(name = "medical_entry_id", nullable = false)
    private MedicalEntryModel medicalEntry;

 
}