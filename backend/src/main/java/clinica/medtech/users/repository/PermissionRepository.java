package clinica.medtech.users.repository;

import clinica.medtech.users.entities.PermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionModel, Long> {
    Optional<PermissionModel> findByName(String create);
}
