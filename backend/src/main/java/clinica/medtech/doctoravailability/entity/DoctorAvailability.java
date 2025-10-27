package clinica.medtech.doctoravailability.entity;

import java.time.LocalTime;

import clinica.medtech.users.entities.ProfessionalModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "doctor_availability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Horario fijo por defecto
    @Column(name = "morning_start", nullable = false)
    private LocalTime morningStart = LocalTime.of(7, 0);

    @Column(name = "morning_end", nullable = false)
    private LocalTime morningEnd = LocalTime.of(12, 0);

    @Column(name = "afternoon_start", nullable = false)
    private LocalTime afternoonStart = LocalTime.of(14, 0);

    @Column(name = "afternoon_end", nullable = false)
    private LocalTime afternoonEnd = LocalTime.of(18, 0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
    private ProfessionalModel doctor;
}