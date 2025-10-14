package clinica.medtech.appointments.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import clinica.medtech.appointments.entity.Appointment;
import clinica.medtech.appointments.enums.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    List<Appointment> findByPatientIdOrderByAppointmentDateTimeDesc(Long patientId);

    List<Appointment> findByDoctorIdOrderByAppointmentDateTimeAsc(Long doctorId);

    List<Appointment> findByDoctorIdAndAppointmentDateTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByStatus(AppointmentStatus status);

    /**
     * Comprueba si existe conflicto para un doctor en una fecha/hora con estados provistos.
     */
    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
            FROM Appointment a
            WHERE a.doctorId = :doctorId
              AND a.status IN :statuses
              AND a.appointmentDateTime = :dateTime
            """)
    boolean existsConflict(@Param("doctorId") Long doctorId,
                           @Param("dateTime") LocalDateTime dateTime,
                           @Param("statuses") List<AppointmentStatus> statuses);

    /**
     * Encuentra la próxima cita confirmada/pendiente para un doctor después de una fecha/hora
     */
    Optional<Appointment> findFirstByDoctorIdAndAppointmentDateTimeAfterAndStatusOrderByAppointmentDateTimeAsc(
            Long doctorId, LocalDateTime dateTime, AppointmentStatus status);

    List<Appointment> findByStatusAndAppointmentDateTimeAfter(AppointmentStatus status, LocalDateTime dateTime);

    boolean existsByDoctorIdAndAppointmentDateTimeAndStatusIn(Long doctorId, LocalDateTime appointmentDateTime,
            List<AppointmentStatus> statuses);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDateTime BETWEEN :start AND :end")
    long countByDoctorIdAndAppointmentDateTimeBetween(@Param("doctorId") Long doctorId,
                                                      @Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);
}
