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

  
}
