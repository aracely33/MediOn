package clinica.medtech.appointments.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import clinica.medtech.appointments.entity.Appointment;
import clinica.medtech.appointments.enums.AppointmentStatus;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

   
    @Query("""
        SELECT a FROM Appointment a
        WHERE a.doctor.id = :doctorId
        AND a.appointmentDate = :date
        AND a.status IN :statuses
        ORDER BY a.appointmentTime
    """)
    List<Appointment> findActiveAppointmentsByDoctorAndDate(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("statuses") List<AppointmentStatus> statuses
    );

    @Query("""
        SELECT a FROM Appointment a
        WHERE a.patient.id = :patientId
        AND a.status <> 'CANCELADA'
        ORDER BY a.appointmentDate ASC, a.appointmentTime ASC
    """)
    List<Appointment> findActiveAppointmentsByPatientId(@Param("patientId") Long patientId);

    @Query("""
        SELECT COUNT(a) > 0
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
        AND a.appointmentDate = :date
        AND a.appointmentTime = :time
        AND a.status IN :statuses
        AND a.id <> :excludeId
    """)
    boolean existsOverlapByDoctorAndTimeExcludingId(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            @Param("excludeId") Long excludeId,
            @Param("statuses") List<AppointmentStatus> statuses
    );
}
