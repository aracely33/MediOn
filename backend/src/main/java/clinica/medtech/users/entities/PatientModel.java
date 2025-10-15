package clinica.medtech.users.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
public class PatientModel extends UserModel {

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String gender;
    private String address;
    private String phone;

    @Column(name = "blood_type")
    private String bloodType;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    private MedicalRecordModel medicalRecord;
}
