package clinica.medtech.appointments.repository;


import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import clinica.medtech.appointments.entity.Appointment;
import clinica.medtech.appointments.enums.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
     List<Appointment> findByPatientIdOrderByAppointmentDateTimeDesc(Long patientId);

    List<Appointment> findByPatientIdAndAppointmentDateTimeGreaterThanEqualOrderByAppointmentDateTimeAsc(
            Long patientId, OffsetDateTime fromDateTime);

    List<Appointment> findByDoctorIdOrderByAppointmentDateTimeAsc(Long doctorId);

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
        FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.status IN :statuses
          AND a.appointmentDateTime < :end
          AND a.endDateTime > :start
        """)
    boolean existsOverlapByDoctorAndTime(
            @Param("doctorId") Long doctorId,
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("statuses") List<AppointmentStatus> statuses
    );

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
        FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.status IN :statuses
          AND a.appointmentDateTime < :end
          AND a.endDateTime > :start
          AND (:excludeId IS NULL OR a.id <> :excludeId)
        """)
    boolean existsOverlapByDoctorAndTimeExcludingId(
            @Param("doctorId") Long doctorId,
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("statuses") List<AppointmentStatus> statuses,
            @Param("excludeId") Long excludeId
    );
}
