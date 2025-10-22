package clinica.medtech.config;

import clinica.medtech.users.Enum.EnumPermission;
import clinica.medtech.users.Enum.EnumRole;
import clinica.medtech.users.entities.*;
import clinica.medtech.users.repository.PatientRepository;
import clinica.medtech.users.repository.PermissionRepository;
import clinica.medtech.users.repository.RoleRepository;
import clinica.medtech.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private final PatientRepository patientRepository;

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

            List<PatientModel> patients = getSamplePatients().stream()
                    .peek(p -> {
                        p.setEmail(p.getName().toLowerCase() + "@example.com");
                        p.setPassword(passwordEncoder.encode("123456"));
                    })
                    .toList();

            patientRepository.saveAll(patients);
            System.out.println("✅ Sample patients created: " + patients.size());
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
    public List<PatientModel> getSamplePatients() {
        return List.of(
                // Ejemplo 1
                PatientModel.builder()
                        .name("Juan")
                        .lastName("Gonzalez")
                        .birthDate(LocalDate.of(1990, 5, 15))
                        .gender("Masculino")
                        .address("Calle Principal 123")
                        .city("Madrid")
                        .country("España")
                        .zip("28001")
                        .bloodType("A+")
                        .phone("1234567890")
                        .email("k3tP1@example.com")
                        .build(),

                // Ejemplo 2
                PatientModel.builder()
                        .name("Maria")
                        .lastName("Perez")
                        .birthDate(LocalDate.of(1985, 10, 20))
                        .gender("Femenino")
                        .address("Avenida Central 456")
                        .city("Buenos Aires")
                        .country("Argentina")
                        .zip("1001")
                        .bloodType("O-")
                        .phone("9876543210")
                        .email("hTf3l@example.com")
                        .build(),

                // Ejemplo 3
                PatientModel.builder()
                        .name("Pedro")
                        .lastName("Garcia")
                        .birthDate(LocalDate.of(1995, 8, 10))
                        .gender("Masculino")
                        .address("Calle Secundaria 789")
                        .city("Ciudad de México")
                        .country("México")
                        .zip("06000")
                        .bloodType("AB+")
                        .phone("5555555555")
                        .email("2l2ZV@example.com")
                        .build(),

                // Ejemplo 4
                PatientModel.builder()
                        .name("Laura")
                        .lastName("Gonzalez")
                        .birthDate(LocalDate.of(1998, 3, 25))
                        .gender("Femenino")
                        .address("Avenida Principal 321")
                        .city("Bogotá")
                        .country("Colombia")
                        .zip("110111")
                        .bloodType("B-")
                        .phone("1111111111")
                        .email("1t7K7@example.com")
                        .build(),

                // Ejemplo 5
                PatientModel.builder()
                        .name("Carlos")
                        .lastName("Lopez")
                        .birthDate(LocalDate.of(1992, 7, 5))
                        .gender("Masculino")
                        .address("Calle Secundaria 654")
                        .city("Lima")
                        .country("Perú")
                        .zip("15001")
                        .bloodType("AB-")
                        .phone("9999999999")
                        .email("t4Yv3@example.com")
                        .build(),

                // Ejemplo 6
                PatientModel.builder()
                        .name("Ana")
                        .lastName("Garcia")
                        .birthDate(LocalDate.of(1988, 12, 15))
                        .gender("Femenino")
                        .address("Avenida Central 987")
                        .city("Santiago")
                        .country("Chile")
                        .zip("8320000")
                        .bloodType("O+")
                        .phone("7777777777")
                        .email("6PcZB@example.com")
                        .build(),

                // Ejemplo 7
                PatientModel.builder()
                        .name("Luis")
                        .lastName("Gonzalez")
                        .birthDate(LocalDate.of(1991, 9, 30))
                        .gender("Masculino")
                        .address("Calle Principal 123")
                        .city("Quito")
                        .country("Ecuador")
                        .zip("170150")
                        .bloodType("A-")
                        .phone("8888888888")
                        .email("4Zs7j@example.com")
                        .build(),

                // Ejemplo 8
                PatientModel.builder()
                        .name("Sofia")
                        .lastName("Perez")
                        .birthDate(LocalDate.of(1997, 4, 20))
                        .gender("Femenino")
                        .address("Avenida Secundaria 456")
                        .city("San José")
                        .country("Costa Rica")
                        .zip("10101")
                        .bloodType("B+")
                        .phone("6666666666")
                        .email("2l2ZV@example.com")
                        .build(),

                // Ejemplo 9
                PatientModel.builder()
                        .name("Diego")
                        .lastName("Garcia")
                        .birthDate(LocalDate.of(1993, 11, 10))
                        .gender("Masculino")
                        .address("Calle Principal 789")
                        .city("Montevideo")
                        .country("Uruguay")
                        .zip("11000")
                        .bloodType("AB+")
                        .phone("9999999999")
                        .email("t4Yv3@example.com")
                        .build(),

                // Ejemplo 10
                PatientModel.builder()
                        .name("Valentina")
                        .lastName("Gonzalez")
                        .birthDate(LocalDate.of(1996, 6, 25))
                        .gender("Femenino")
                        .address("Avenida Central 321")
                        .city("Asunción")
                        .country("Paraguay")
                        .zip("1209")
                        .bloodType("O-")
                        .phone("7777777777")
                        .email("6PcZB@example.com")
                        .build()
        );
    }
}

