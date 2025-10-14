package clinica.medtech.config;

import clinica.medtech.users.Enum.EnumPermission;
import clinica.medtech.users.Enum.EnumRole;
import clinica.medtech.users.entities.PermissionModel;
import clinica.medtech.users.entities.ProfessionalModel;
import clinica.medtech.users.entities.RoleModel;
import clinica.medtech.users.entities.UserModel;
import clinica.medtech.users.repository.PermissionRepository;
import clinica.medtech.users.repository.RoleRepository;
import clinica.medtech.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
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

            RoleModel professionalRole = roleRepository.findByEnumRole(EnumRole.PROFESSIONAL)
                    .orElseThrow(() -> new RuntimeException("❌ El rol PROFESSIONAL no está en la base de datos."));


            UserModel admin = UserModel.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(adminRole))
                    .build();



            userRepository.save(admin);

            System.out.println("✅ Admin user created.");

            List<ProfessionalModel> professionals = getSampleProfessionals().stream()
                    .peek(p -> {
                        p.setEmail(p.getSpecialty().toLowerCase() + "@example.com");
                        p.setPassword(passwordEncoder.encode("123456"));
                        p.setRoles(Set.of(professionalRole));
                    })
                    .toList();

            userRepository.saveAll(professionals);
            System.out.println("✅ Sample professionals created: " + professionals.size());
        }
    }

    public List<ProfessionalModel> getSampleProfessionals() {
        return List.of(
                // Ejemplo 1
                ProfessionalModel.builder()
                        .name("Dr. Juan")
                        .lastName("Lopez")
                        .medicalLicense("123456")
                        .specialty("Cardiologo")
                        .biography("Soy un médico cardiólogo con más de 10 años de experiencia.")
                        .consultationFee(BigDecimal.valueOf(1000.00))
                        .build(),

                // Ejemplo 2
                ProfessionalModel.builder()
                        .name("Dr. Maria")
                        .lastName("Perez")
                        .medicalLicense("654321")
                        .specialty("Dermatóloga")
                        .biography("Especialista en el cuidado de la piel, pelo y uñas. Ofrezco tratamientos de última generación.")
                        .consultationFee(BigDecimal.valueOf(1200.50))
                        .build(),

                // Ejemplo 3
                ProfessionalModel.builder()
                        .name("Dr. Pedro")
                        .lastName("Garcia")
                        .medicalLicense("987654")
                        .specialty("Oftalmólogo")
                        .biography("Diagnóstico y tratamiento de enfermedades oculares. Experto en cirugía refractiva.")
                        .consultationFee(BigDecimal.valueOf(1500.00))
                        .build(),

                // Ejemplo 4
                ProfessionalModel.builder()
                        .name("Dr. Laura")
                        .lastName("Gonzalez")
                        .medicalLicense("456789")
                        .specialty("Pediatra")
                        .biography("Atención integral para niños y adolescentes. Mi objetivo es el bienestar de los más pequeños.")
                        .consultationFee(BigDecimal.valueOf(950.00))
                        .build(),

                // Ejemplo 5
                ProfessionalModel.builder()
                        .name("Dr. Ana")
                        .lastName("Rodriguez")
                        .medicalLicense("321987")
                        .specialty("Ginecóloga")
                        .biography("Salud femenina en todas las etapas. Controles, embarazos y menopausia.")
                        .consultationFee(BigDecimal.valueOf(1150.75))
                        .build(),

                // Ejemplo 6
                ProfessionalModel.builder()
                        .name("Dr. Luis")
                        .lastName("Martinez")
                        .medicalLicense("789123")
                        .specialty("Psiquiatra")
                        .biography("Terapia y apoyo en trastornos mentales. Ayudo a mis pacientes a recuperar el equilibrio emocional.")
                        .consultationFee(BigDecimal.valueOf(1800.00))
                        .build(),

                // Ejemplo 7
                ProfessionalModel.builder()
                        .name("Dr. Carolina")
                        .lastName("Sanchez")
                        .medicalLicense("147258")
                        .specialty("Ortopedista")
                        .biography("Especialista en lesiones musculoesqueléticas. Tratamientos para fracturas y problemas de articulaciones.")
                        .consultationFee(BigDecimal.valueOf(1350.25))
                        .build(),

                // Ejemplo 8
                ProfessionalModel.builder()
                        .name("Dr. Miguel")
                        .lastName("Gonzalez")
                        .medicalLicense("369852")
                        .specialty("Neurólogo")
                        .biography("Experto en enfermedades del sistema nervioso. Diagnóstico y tratamiento de migrañas y epilepsia.")
                        .consultationFee(BigDecimal.valueOf(1700.00))
                        .build(),

                // Ejemplo 9
                ProfessionalModel.builder()
                        .name("Dr. Laura")
                        .lastName("Fernandez")
                        .medicalLicense("258741")
                        .specialty("Endocrinólogo")
                        .biography("Manejo de trastornos hormonales y metabólicos como la diabetes y problemas de tiroides.")
                        .consultationFee(BigDecimal.valueOf(1400.50))
                        .build(),

                // Ejemplo 10
                ProfessionalModel.builder()
                        .name("Dr. Carlos")
                        .lastName("Lopez")
                        .medicalLicense("852963")
                        .specialty("Nutricionista")
                        .biography("Asesoramiento nutricional personalizado. Ayudo a mis pacientes a alcanzar sus metas de salud y peso.")
                        .consultationFee(BigDecimal.valueOf(850.00))
                        .build()
        );
    }
}

