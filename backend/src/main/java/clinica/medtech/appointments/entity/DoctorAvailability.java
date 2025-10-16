package clinica.medtech.appointments.entity;

import java.time.DayOfWeek;
import java.time.OffsetTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "doctor_availability")
public class DoctorAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id",nullable = false)
    private Long doctorId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week",nullable = false)
    private DayOfWeek dayOfWeek;
    
    @Column(name = "start_time",nullable = false, columnDefinition = "TIME WITH TIME ZONE")
    private OffsetTime startTime;
    
    @Column(name = "end_time",nullable = false, columnDefinition = "TIME WITH TIME ZONE")
    private OffsetTime endTime;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
