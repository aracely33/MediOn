package clinica.medtech.users.controller;

import clinica.medtech.auth.jwt.JwtUtils;
import clinica.medtech.users.dtoRequest.AuthLoginRequestDto;
import clinica.medtech.users.dtoRequest.PatientRequestDto;
import clinica.medtech.users.dtoRequest.SuspendRequestDto;
import clinica.medtech.users.dtoRequest.UserMeRequestDto;
import clinica.medtech.users.dtoResponse.AuthResponseDto;
import clinica.medtech.users.dtoResponse.AuthResponseRegisterDto;
import clinica.medtech.users.dtoResponse.UserMeResponseDto;
import clinica.medtech.users.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API")
@Validated
@RequiredArgsConstructor
public class AuthController {
    //private final ProfessionalService professionalService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    /**
     * Inicia sesión y obtiene un token de autenticación.
     *
     * @param authDto credenciales de login
     * @return un ResponseEntity que contiene el token de autenticación
     */
    @Operation(summary = "Iniciar sesión", description = "Inicia sesión y obtiene un token de autenticación." +
            " Por defecto ya se encuentra registrado un ADMIN con credenciales de login: " +
            "email: admin@example.com, password: admin123")
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa")
    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody @Valid AuthLoginRequestDto authDto,
            HttpServletResponse servletResponse) {

        AuthResponseDto response = this.userDetailsService.loginUser(authDto);

        ResponseCookie cookie = ResponseCookie.from("jwt_token", response.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtUtils.getExpirationTime())
                .sameSite("None")
                .build();

        servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok()
                .header("user-id", response.getId().toString())
                .body(response);
    }

    /**
     * Registra un nuevo profesional y obtiene un token de autenticación.
     *
     * @param authCreateUserDto credenciales de registro
     * @return un ResponseEntity que contiene el token de autenticación
     */
    @Operation(summary = "Registrar nuevo paciente", description = """
            Registra un nuevo paciente y obtiene un token de autenticación.
            """)
    @PostMapping(value = "/patient/register")
    public ResponseEntity<AuthResponseRegisterDto> registerProfessional(
            @RequestBody @Valid PatientRequestDto authCreateUserDto,
            HttpServletResponse servletResponse) {

        AuthResponseRegisterDto response = this.userDetailsService.createUser(authCreateUserDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Obtiene los datos del usuario autenticado.
     *
     * @return un ResponseEntity que contiene los datos del usuario autenticado
     */
    @Operation(summary = "Obtener el usuario actual", description = """
            Retorna los datos del usuario autenticado.
            """)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserMeResponseDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(userDetailsService.getCurrentUser(email));
    }

    /**
     * Actualiza los datos del usuario autenticado.
     *
     * @param userId el ID del usuario a actualizar
     * @param userMeRequestDto los nuevos datos del usuario
     * @return un ResponseEntity que contiene los datos actualizados del usuario
     */
    /*@Operation(summary = "Actualizar el usuario actual", description = """
            Actualiza los datos del usuario autenticado.
            """)
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me/{userId}")
    public ResponseEntity<UserMeResponseDto> updateCurrentUser(@PathVariable Long userId, @RequestBody @Valid UserMeRequestDto userMeRequestDto) {
        UserMeResponseDto userMeResponseDto = userDetailsService.updateCurrentUser(userId, userMeRequestDto);
        return ResponseEntity.ok(userMeResponseDto);
    }*/

    /**
     * Cierra la sesión del usuario actual.
     *
     * @param servletResponse la respuesta HTTP
     * @return un ResponseEntity que indica que la sesión se ha cerrado correctamente
     */
    @Operation(summary = "Cerrar sesión", description = """
            Cierra la sesión del usuario actual.
            """)
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(HttpServletResponse servletResponse) {
        ResponseCookie cookie = ResponseCookie.from("jwt_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();
        servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Suspender un usuario", description = """
            Suspende a un usuario por una duración y unidad de tiempo especificadas. Solo el ADMIN puede suspender un usuario. Ten en cuenta que la unidad 
            de tiempo puede ser "HOURS", "DAYS", "WEEKS" o "MONTHS".
             En ese lazo de tiempo el usuario no puede iniciar sesión.
            """)

    @PostMapping("/{userId}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    /**
     * Suspende a un usuario por una duración y unidad de tiempo especificadas.
     *
     * @param userId el ID del usuario a suspender
     * @param request la solicitud de suspensión que contiene la duración y la unidad de tiempo
     * @return un ResponseEntity que contiene la hora de finalización de la suspensión y el tiempo restante
     */
    public ResponseEntity<?> suspendUser(@PathVariable Long userId,
                                         @RequestBody @Valid SuspendRequestDto request) {
        userDetailsService.suspendUser(userId, request.getDuration(), request.getUnit());

        LocalDateTime suspensionEndTime = LocalDateTime.now().plus(request.getDuration(), mapToChronoUnit(request.getUnit()));
        long remainingTime = ChronoUnit.SECONDS.between(LocalDateTime.now(), suspensionEndTime);

        return ResponseEntity.ok().body("Usuario suspendido hasta: " +
                suspensionEndTime + " (Tiempo restante: " + remainingTime + " segundos)");
    }

    /**
     * Reactiva a un usuario suspendido.
     *
     * @param userId el ID del usuario a reactivar
     * @return un ResponseEntity que indica que el usuario ha sido reactivado
     */
    @Operation(summary = "Reactivar un usuario", description = """
            Reactiva a un usuario suspendido.
            """)
    @PostMapping("/{userId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        userDetailsService.activateUser(userId);
        return ResponseEntity.ok().body("Usuario reactivado exitosamente");
    }

    /**
     * Mapea una unidad de tiempo a un ChronoUnit.
     *
     * @param unit la unidad de tiempo
     * @return el ChronoUnit correspondiente
     */
    private ChronoUnit mapToChronoUnit(SuspendRequestDto.TimeUnit unit) {
        return switch (unit) {
            case HOURS -> ChronoUnit.HOURS;
            case DAYS -> ChronoUnit.DAYS;
            case WEEKS -> ChronoUnit.WEEKS;
            case MONTHS -> ChronoUnit.MONTHS;
        };
    }


}
