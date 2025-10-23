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

    // 🔹 Verifica si hay conflicto de horario para un doctor (al crear cita)
    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
        FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.status IN :statuses
          AND (
              (a.appointmentDate = :startDate AND a.appointmentTime BETWEEN :startTime AND :endTime)
              OR (a.appointmentDate = :endDate AND a.appointmentTime BETWEEN :startTime AND :endTime)
              OR (a.appointmentDate > :startDate AND a.appointmentDate < :endDate)
          )
    """)
    boolean existsOverlapByDoctorAndTime(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("startTime") LocalTime startTime,
            @Param("endDate") LocalDate endDate,
            @Param("endTime") LocalTime endTime,
            @Param("statuses") List<AppointmentStatus> statuses
    );

    // 🔹 Verifica conflicto de horario al modificar una cita (excluyendo su propio ID)
    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
        FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.status IN :statuses
          AND (
              (a.appointmentDate = :startDate AND a.appointmentTime BETWEEN :startTime AND :endTime)
              OR (a.appointmentDate = :endDate AND a.appointmentTime BETWEEN :startTime AND :endTime)
              OR (a.appointmentDate > :startDate AND a.appointmentDate < :endDate)
          )
          AND (:excludeId IS NULL OR a.id <> :excludeId)
    """)
    boolean existsOverlapByDoctorAndTimeExcludingId(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("startTime") LocalTime startTime,
            @Param("endDate") LocalDate endDate,
            @Param("endTime") LocalTime endTime,
            @Param("statuses") List<AppointmentStatus> statuses,
            @Param("excludeId") Long excludeId
    );

    // 🔹 Obtiene solo las citas activas del doctor
    @Query("""
        SELECT a FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.status IN :statuses
        ORDER BY a.appointmentDate ASC, a.appointmentTime ASC
    """)
    List<Appointment> findActiveAppointmentsByDoctor(
            @Param("doctorId") Long doctorId,
            @Param("statuses") List<AppointmentStatus> statuses
    );

    // 🔹 Busca todas las citas de un paciente
    @Query("""
        SELECT a FROM Appointment a
        WHERE a.patientId = :patientId
        ORDER BY a.appointmentDate ASC, a.appointmentTime ASC
    """)
    List<Appointment> findAppointmentsByPatientId(@Param("patientId") Long patientId);
}