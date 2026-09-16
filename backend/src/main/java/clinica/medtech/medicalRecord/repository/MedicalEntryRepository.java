package clinica.medtech.medicalRecord.repository;

import clinica.medtech.medicalRecord.entities.MedicalEntryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalEntryRepository extends JpaRepository<MedicalEntryModel, Long> {
}