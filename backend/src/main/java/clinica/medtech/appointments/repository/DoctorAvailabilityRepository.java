package clinica.medtech.appointments.repository;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import clinica.medtech.appointments.entity.DoctorAvailability;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long>{
    List<DoctorAvailability> findByDoctorId(Long doctorId);

    List<DoctorAvailability> findByDoctorIdAndIsActiveTrue(Long doctorId);

    List<DoctorAvailability> findByDoctorIdAndDayOfWeekAndIsActiveTrue(Long doctorId, DayOfWeek dayOfWeek);

    boolean existsByDoctorIdAndDayOfWeekAndStartTimeAndEndTime(
        Long doctorId, DayOfWeek dayOfWeek, OffsetTime startTime, OffsetTime endTime);
}
