package clinica.medtech.users.repository;


import clinica.medtech.users.Enum.EnumRole;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);

    List<UserModel> findAllBySuspensionEndBefore(LocalDateTime now);

    List<UserModel> findUsersByRolesEnumRole(EnumRole enumRole);

    // Filtrar pacientes por nombre
    List<PatientModel> findByNameAndRolesEnumRole(String name, EnumRole enumRole);

    // Filtrar pacientes por ID
    Optional<PatientModel> findByIdAndRolesEnumRole(Long id, EnumRole enumRole);

    // Filtrar pacientes por email
    Optional<PatientModel> findByEmailAndRolesEnumRole(String email, EnumRole enumRole);

    // Filtrar pacientes por apellido
    List<PatientModel> findByLastNameAndRolesEnumRole(String lastName, EnumRole enumRole);
}
