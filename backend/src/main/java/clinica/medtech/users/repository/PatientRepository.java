package clinica.medtech.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import clinica.medtech.users.entities.PatientModel;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientModel, Long> {
    List<PatientModel> findByName(String name);

    Optional<PatientModel> findById(Long id);

    Optional<PatientModel> findByEmail(String email);

    List<PatientModel> findByLastName(String lastName);
}