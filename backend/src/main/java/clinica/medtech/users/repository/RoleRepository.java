package clinica.medtech.users.repository;



import clinica.medtech.users.Enum.EnumRole;
import clinica.medtech.users.entities.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    List<RoleModel> findRoleEntitiesByEnumRoleIn(List<String> roles);
    Optional<RoleModel> findByEnumRole(EnumRole role);



}
