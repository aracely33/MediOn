package clinica.medtech.users.service;

import clinica.medtech.auth.jwt.JwtUtils;
import clinica.medtech.exceptions.EmailAlreadyExistsException;
import clinica.medtech.notifications.service.EmailService;
import clinica.medtech.notifications.service.Impl.EmailVerificationService;
import clinica.medtech.users.Enum.EnumRole;
import clinica.medtech.users.dtoRequest.AuthLoginRequestDto;
import clinica.medtech.users.dtoRequest.PatientRequestDto;
import clinica.medtech.users.dtoRequest.PatientUpdateRequestDto;
import clinica.medtech.users.dtoRequest.SuspendRequestDto;
import clinica.medtech.users.dtoRequest.UserMeRequestDto;
import clinica.medtech.users.dtoResponse.AuthResponseDto;
import clinica.medtech.users.dtoResponse.AuthResponseRegisterDto;
import clinica.medtech.users.dtoResponse.PatientMeResponseDto;
import clinica.medtech.users.dtoResponse.PatientMeResponseDto;
import clinica.medtech.users.dtoResponse.UserMeResponseDto;
import clinica.medtech.users.dtoResponse.UserResponseDto;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.entities.RoleModel;
import clinica.medtech.users.entities.UserModel;
import clinica.medtech.users.repository.PatientRepository;
import clinica.medtech.users.repository.RoleRepository;
import clinica.medtech.users.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                "El usuario con el email " + email + "no existe"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userEntity.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getEnumRole().name())));
        });

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                true,
                true,
                true,
                authorities);
    }

    public AuthResponseDto loginUser(@Valid AuthLoginRequestDto authDto) {
        String email = authDto.getEmail().trim().toLowerCase();
        String password = authDto.getPassword();

        Long id = userRepository.findByEmail(email)
                .map(UserModel::getId)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con el correo " + email + " no existe"));

        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateJwtToken(authentication);

        return new AuthResponseDto(id, email, "Autenticación exitosa", token, true);
    }


    /**
     * Crea un nuevo usuario paciente, asociando un usuario en la base de datos.
     * Valida que el email no estén registrados previamente.
     *
     * @param authCreateUserDto DTO con la información del profesional a registrar.
     * @return DTO de respuesta con información del registro y token JWT.
     * @throws EmailAlreadyExistsException si el correo ya está registrado.
     */
    @Transactional
    public AuthResponseRegisterDto createUser(@Valid PatientRequestDto authCreateUserDto) {

        // Normalizar el email
        String email = authCreateUserDto.getEmail().trim().toLowerCase();
        String name = authCreateUserDto.getName().trim();
        String lastName = authCreateUserDto.getLastName().trim();
        String password = authCreateUserDto.getPassword();

        //Verificar si ya existe (en minúsculas)
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("El correo " + email + " ya existe en la base de datos.");
        }

        // Buscar rol del paciente
        RoleModel patientRole = roleRepository.findByEnumRole(EnumRole.PATIENT)
                .orElseThrow(() -> new IllegalArgumentException("El rol especificado no está configurado en la base de datos."));

        Set<RoleModel> roleEntities = Set.of(patientRole);

        // Crear entidad con email normalizado
        PatientModel patientEntity = PatientModel.builder()
                .email(email)
                .name(name)
                .lastName(lastName)
                .password(passwordEncoder.encode(password))
                .roles(roleEntities)
                .emailVerified(false)
                .build();

        log.info("Registrando paciente: {}", patientEntity.getRoles().stream().map(RoleModel::getEnumRole).toList());
        PatientModel patientCreated = patientRepository.save(patientEntity);

        try {
            emailService.sendWelcomeEmail(patientCreated.getEmail(), patientCreated.getName());
            // Agregar verificación por código (opcional por ahora)
            emailVerificationService.createVerificationCode(patientCreated);
        } catch (Exception e) {
            log.warn("No se pudo enviar el email de bienvenida a: {}", patientCreated.getEmail(), e);
            log.warn("No se pudo enviar emails a: {}", patientCreated.getEmail(), e);
        }

        // Generar authorities
        List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();
        patientCreated.getRoles().forEach(role -> {
            authoritiesList.add(new SimpleGrantedAuthority("ROLE_" + role.getEnumRole().name()));
            role.getPermissions().forEach(permission ->
                    authoritiesList.add(new SimpleGrantedAuthority(permission.getName())));
        });

        UserDetails userDetails = loadUserByUsername(patientCreated.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, patientCreated.getPassword(), authoritiesList);

        String accessToken = jwtUtils.generateJwtToken(authentication);

        return new AuthResponseRegisterDto(
                patientCreated.getId(),
                patientCreated.getName(),
                "Paciente registrado exitosamente",
                null,
                true
        );
    }



    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);

        UserModel userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!userEntity.isEnabled()) {
            throw new DisabledException("Usuario suspendido hasta: " + userEntity.getSuspensionEnd());
        }


        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

    }


    public List<UserResponseDto> getUsersByRoleAdmin() {
        List<UserModel> users = userRepository.findUsersByRolesEnumRole(EnumRole.ADMIN);
        List<UserResponseDto> userResponseDtos = new ArrayList<>();

        users.forEach(user -> {
            userResponseDtos.add(new UserResponseDto(
                    user.getName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRoles().stream().map(role -> role.getEnumRole().name()).toList()
            ));
        });

        return userResponseDtos;
    }


    public UserMeResponseDto getCurrentUser(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con el email " + email + " no encontrado"));

        return UserMeResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .lastName(user.getLastName())
                .roles(user.getRoles().stream()
                        .map(role -> role.getEnumRole().name())
                        .toList())
                .build();
    }

    @Transactional
    public UserMeResponseDto updateCurrentUser(Long id, UserMeRequestDto userMeRequest) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con el id " + id + " no encontrado"));

        if (!user.getEmail().equals(userMeRequest.getEmail())) {
            userRepository.findByEmail(userMeRequest.getEmail()).ifPresent(existingUser -> {
                throw new EmailAlreadyExistsException("El correo " + userMeRequest.getEmail() + " ya existe en la base de datos.");
            });
            user.setEmail(userMeRequest.getEmail());
        }


        user.setName(userMeRequest.getName());
        user.setLastName(userMeRequest.getLastName());

        userRepository.save(user);
        return getCurrentUser(user.getEmail());
    }



    @Transactional
    public void suspendUser(Long userId, int duration, SuspendRequestDto.TimeUnit unit) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        LocalDateTime now = LocalDateTime.now();
        user.setSuspensionEnd(calculateSuspensionEnd(now, duration, unit));
        userRepository.save(user);
    }

    private LocalDateTime calculateSuspensionEnd(LocalDateTime start, int duration, SuspendRequestDto.TimeUnit unit) {
        return switch (unit) {
            case HOURS -> start.plusHours(duration);
            case DAYS -> start.plusDays(duration);
            case WEEKS -> start.plusWeeks(duration);
            case MONTHS -> start.plusMonths(duration);
        };
    }

    @Transactional
    public void activateUser(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        user.setSuspensionEnd(null);
        userRepository.save(user);
    }
}
