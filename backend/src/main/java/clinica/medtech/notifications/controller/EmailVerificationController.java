package clinica.medtech.notifications.controller;


import clinica.medtech.notifications.Dto.VerifyEmailRequestDto;
import clinica.medtech.notifications.service.Impl.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Email Verification", description = "Verificación de correo electrónico")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @Operation(summary = "Verificar email con código", description = "Verifica la dirección de correo electrónico usando el código de 6 dígitos enviado")
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody @Valid VerifyEmailRequestDto request) {
        boolean verified = emailVerificationService.verifyEmailWithCode(request.getVerificationCode());

        if (verified) {
            return ResponseEntity.ok("Email verificado exitosamente");
        } else {
            return ResponseEntity.badRequest().body("Código de verificación inválido o expirado");
        }
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
