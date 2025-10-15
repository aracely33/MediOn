package clinica.medtech.users.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("PATIENT")
@SuperBuilder
@Table(name = "patients")
public class PatientModel extends UserModel {

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String gender;
    private String address;
    private String phone;

    @Column(name = "blood_type")
    private String bloodType;
}
