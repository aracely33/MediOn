package clinica.medtech.doctoravailability.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import clinica.medtech.doctoravailability.entity.DoctorAvailability;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
    Optional<DoctorAvailability> findByDoctorIdAndIsActiveTrue(Long doctorId);
}