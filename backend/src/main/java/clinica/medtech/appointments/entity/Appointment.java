package clinica.medtech.appointments.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import clinica.medtech.appointments.enums.AppointmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    @Column(name = "appointment_date_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime appointmentDateTime;

    @Column(name = "end_date_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime endDateTime;

    @Column(name = "duration", nullable = false)
    private Integer duration; // en minutos

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Calcula automáticamente la hora de finalización antes de guardar o actualizar.
     * Si no se define duración, se asignan 30 minutos por defecto.
     */
    @PrePersist
    @PreUpdate
    private void calculateEndDateTime() {
        if (appointmentDateTime != null) {
            if (duration == null) duration = 30;
            this.endDateTime = appointmentDateTime.plusMinutes(duration);
        }
    }
}