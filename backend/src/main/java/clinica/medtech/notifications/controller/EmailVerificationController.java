package clinica.medtech.notifications.controller;


import clinica.medtech.auth.jwt.JwtUtils;
import clinica.medtech.notifications.Dto.VerifyEmailRequestDto;
import clinica.medtech.notifications.service.Impl.EmailVerificationService;
import clinica.medtech.users.dtoResponse.AuthResponseDto;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.entities.UserModel;
import clinica.medtech.users.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Email Verification", description = "Verificación de correo electrónico")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Operation(summary = "Verificar email con código", description = "Verifica la dirección de correo electrónico usando el código de 6 dígitos enviado")
    @PostMapping("/verify-email")
    public ResponseEntity<AuthResponseDto> verifyEmail(@RequestBody @Valid VerifyEmailRequestDto request) {
        Optional<UserModel> optionalUser = emailVerificationService.verifyEmail(request.getVerificationCode());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponseDto(null, null, "Código inválido o expirado", null, false));
        }

        UserModel user = optionalUser.get();

        // Generar JWT aquí en el controlador
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        String token = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new AuthResponseDto(
                user.getId(),
                user.getName(),
                "Email verificado exitosamente",
                token,
                true
        ));
    }



    @Operation(summary = "Reenviar código de verificación", description = "Reenvía el código de verificación al usuario")
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationCode(@RequestParam String email) {
        try {
            emailVerificationService.resendVerificationCode(email);
            return ResponseEntity.ok("Código de verificación reenviado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
