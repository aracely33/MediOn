package clinica.medtech.doctoravailability.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clinica.medtech.doctoravailability.entity.DoctorAvailability;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long>{


    List<DoctorAvailability> findByDoctorIdAndIsActiveTrue(Long doctorId);

    Optional<DoctorAvailability> findByIdAndIsActiveTrue(Long id);
}
