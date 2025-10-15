package clinica.medtech.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import clinica.medtech.users.entities.PatientModel;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientModel, Long> {
    Optional<PatientModel> findByEmailIgnoreCase(String email);
    List<PatientModel> findByNameContainingIgnoreCase(String name);
    List<PatientModel> findByLastNameContainingIgnoreCase(String lastName);
    Page<PatientModel> findByBloodTypeContainingIgnoreCase(String bloodType, Pageable pageable);
}