package clinica.medtech.users.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "professionals")
public class ProfessionalModel extends UserModel {

    private String medicalLicense;
    private String specialty;
    private String biography;

    @Column(name = "consultation_fee")
    private BigDecimal consultationFee;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    private List<MedicalEntryModel> medicalEntries;
}
