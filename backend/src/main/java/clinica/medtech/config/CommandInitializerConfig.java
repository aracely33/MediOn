package clinica.medtech.config;

import clinica.medtech.users.Enum.EnumPermission;
import clinica.medtech.users.Enum.EnumRole;
import clinica.medtech.users.modules.PermissionModel;
import clinica.medtech.users.modules.RoleModel;
import clinica.medtech.users.modules.UserModel;
import clinica.medtech.users.repository.PermissionRepository;
import clinica.medtech.users.repository.RoleRepository;
import clinica.medtech.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandInitializerConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (permissionRepository.count() == 0) {
            for (EnumPermission permission : EnumPermission.values()) {
                PermissionModel newPermission = new PermissionModel();
                newPermission.setName(permission.name());
                permissionRepository.save(newPermission);
            }
            System.out.println("✅ Permissions initialized.");
        }

        if (roleRepository.count() == 0) {
            Set<PermissionModel> allPermissions = new HashSet<>(permissionRepository.findAll());

            RoleModel adminRole = new RoleModel(EnumRole.ADMIN);
            adminRole.setPermissions(allPermissions);
            roleRepository.save(adminRole);

            RoleModel professionalRole = new RoleModel(EnumRole.PROFESSIONAL);
            roleRepository.save(professionalRole);

            RoleModel patientRole = new RoleModel(EnumRole.PATIENT);
            roleRepository.save(patientRole);

            System.out.println("✅ Roles initialized.");
        }

        if (userRepository.count() == 0) {
            RoleModel adminRole = roleRepository.findByEnumRole(EnumRole.ADMIN)
                    .orElseThrow(() -> new RuntimeException("❌ El rol ADMIN no está en la base de datos."));


            UserModel admin = UserModel.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(admin);

            System.out.println("✅ Admin user created.");
        }
    }
}

