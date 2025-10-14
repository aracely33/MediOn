package clinica.medtech.appointments.repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import clinica.medtech.appointments.entity.DoctorAvailability;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long>{
    List<DoctorAvailability> findByDoctorIdAndIsActiveTrueOrderByDayOfWeekAscStartTimeAsc(Long doctorId);

    List<DoctorAvailability> findByDoctorIdAndDayOfWeekAndIsActiveTrueOrderByStartTimeAsc(Long doctorId, DayOfWeek dayOfWeek);

    Optional<DoctorAvailability> findByIdAndIsActiveTrue(Long id);

    /**
     * Comprueba si existe una disponibilidad activa del doctor en el mismo día que se solapa con el rango [startTime, endTime)
     * Condición de solapamiento: existing.start < newEnd AND existing.end > newStart
     */
    @Query("""
        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
        FROM DoctorAvailability d
        WHERE d.doctorId = :doctorId
          AND d.dayOfWeek = :dayOfWeek
          AND d.isActive = true
          AND d.startTime < :endTime
          AND d.endTime > :startTime
        """)
    boolean existsOverlap(
            @Param("doctorId") Long doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    /**
     * Igual que existsOverlap pero excluye un registro por id (útil al actualizar una disponibilidad existente)
     */
    @Query("""
        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
        FROM DoctorAvailability d
        WHERE d.doctorId = :doctorId
          AND d.dayOfWeek = :dayOfWeek
          AND d.isActive = true
          AND d.startTime < :endTime
          AND d.endTime > :startTime
          AND d.id <> :excludeId
        """)
    boolean existsOverlapExcludingId(
            @Param("doctorId") Long doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") Long excludeId
    );
}
