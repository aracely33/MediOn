package clinica.medtech.medicalRecord.repository;

import clinica.medtech.medicalRecord.entities.DiagnosisModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisRepository extends JpaRepository<DiagnosisModel, Long> {
}