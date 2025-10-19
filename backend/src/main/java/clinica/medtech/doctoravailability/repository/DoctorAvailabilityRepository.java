package clinica.medtech.doctoravailability.repository;

import java.time.DayOfWeek;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clinica.medtech.doctoravailability.entity.DoctorAvailability;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long>{
    // 🟢 Obtener todas las disponibilidades de un doctor
    List<DoctorAvailability> findByDoctorId(Long doctorId);

    // 🟢 Obtener solo las disponibilidades activas de un doctor
    List<DoctorAvailability> findByDoctorIdAndIsActiveTrue(Long doctorId);

    // 🟢 Buscar disponibilidades por doctor y día (útil para validaciones)
    List<DoctorAvailability> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
}
