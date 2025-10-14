package clinica.medtech.users.repository;

import clinica.medtech.users.entities.ProfessionalModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<ProfessionalModel, Long> {


    // Buscar por matrícula (única)
    Optional<ProfessionalModel> findByMedicalLicense(String medicalLicense);

    // Buscar por email (único)
    Optional<ProfessionalModel> findByEmailIgnoreCase(String email);

    // Buscar por nombre (parcial, sin distinción de mayúsculas)
    @Query("SELECT p FROM ProfessionalModel p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ProfessionalModel> findByNameContainingIgnoreCase(String name);

    // Buscar por especialidad con paginación
    @Query("SELECT p FROM ProfessionalModel p WHERE LOWER(p.specialty) LIKE LOWER(CONCAT('%', :specialty, '%'))")
    Page<ProfessionalModel> findBySpecialtyContainingIgnoreCase(String specialty, Pageable pageable);
}
