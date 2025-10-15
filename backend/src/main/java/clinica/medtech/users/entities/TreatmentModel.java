
package clinica.medtech.users.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

/* class Tratamiento {
    - id: String
    - tipo: String
    - descripcion: String
    - medicamento: String
    - dosis: String
    - frecuencia: String
    - inicio: Date
    - fin: Date
    - fhirId: String
    + convertirAFHIR(): String
  }
 */


@Entity
@Table(name = "treatments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "medication")
    private String medication;

    @Column(name = "dose")
    private String dose;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "start_date")
    private Date start;

    @Column(name = "end_date")
    private Date end;

    @Column(name = "fhir_id", unique = true)
    private String fhirId;


    @ManyToOne
    @JoinColumn(name = "medical_entry_id", nullable = false)
    private MedicalEntryModel medicalEntry;



  
}