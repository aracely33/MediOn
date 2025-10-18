package clinica.medtech.medicalRecord.repository;

import clinica.medtech.medicalRecord.entities.MedicalRecordModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecordModel, Long> {
}