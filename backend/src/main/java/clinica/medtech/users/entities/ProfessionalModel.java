package clinica.medtech.users.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("PROFESSIONAL")
@Table(name = "professionals")
public class ProfessionalModel extends UserModel {

    private String medicalLicense;
    private String specialty;
    private String biography;

    @Column(name = "consultation_fee")
    private BigDecimal consultationFee;
}
