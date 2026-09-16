package clinica.medtech.medicalRecord.repository;

import clinica.medtech.medicalRecord.entities.TreatmentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentRepository extends JpaRepository<TreatmentModel, Long> {
}