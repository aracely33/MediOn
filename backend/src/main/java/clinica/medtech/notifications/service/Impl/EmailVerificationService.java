package clinica.medtech.notifications.service.Impl;

import clinica.medtech.notifications.entities.EmailVerificationToken;
import clinica.medtech.notifications.repository.EmailVerificationTokenRepository;
import clinica.medtech.notifications.service.EmailService;
import clinica.medtech.users.entities.UserModel;
import clinica.medtech.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public void createVerificationCode(UserModel user) {
        try {
            // Eliminar códigos anteriores si existen
            tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

            // Generar código de 6 dígitos
            String verificationCode = generateVerificationCode();

            EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                    .verificationCode(verificationCode)
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusMinutes(15))  //tiempo de expirancion de codigo
                    .used(false)
                    .createdDate(LocalDateTime.now())
                    .build();

            EmailVerificationToken savedToken = tokenRepository.save(verificationToken);

            // Solo enviar email si se guardó correctamente
            if (savedToken.getId() != null) {
                sendVerificationEmail(user, verificationCode);
                log.info("Código de verificación creado y enviado para usuario: {}", user.getEmail());
            } else {
                log.error("Error: No se pudo guardar el token de verificación para: {}", user.getEmail());
            }

        } catch (Exception e) {
            log.error("Error creando código de verificación para usuario: {}", user.getEmail(), e);

        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // generar el codigo random
        return String.valueOf(code);
    }

    private void sendVerificationEmail(UserModel user, String verificationCode) {
        try {
            emailService.sendVerificationEmail(user.getEmail(), user.getName(), verificationCode);
            log.info("Código de verificación enviado a: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error enviando código de verificación a: {}", user.getEmail(), e);
        }
    }

    @Transactional
    public boolean verifyEmailWithCode(String verificationCode) {
        EmailVerificationToken verificationToken = tokenRepository.findByVerificationCode(verificationCode)
                .orElse(null);

        if (verificationToken == null) {
            log.warn("Código de verificación no encontrado: {}", verificationCode);
            return false;
        }

        if (verificationToken.getUsed()) {
            log.warn("Código de verificación ya utilizado: {}", verificationCode);
            return false;
        }

        if (verificationToken.isExpired()) {
            log.warn("Código de verificación expirado: {}", verificationCode);
            return false;
        }

        // Marcar usuario como verificado
        UserModel user = verificationToken.getUser();
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        // Marcar código como usado
        verificationToken.setUsed(true);
        tokenRepository.save(verificationToken);

        log.info("Email verificado exitosamente para usuario: {}", user.getEmail());
        return true;
    }

    @Transactional
    public void resendVerificationCode(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getEmailVerified()) {
            throw new RuntimeException("El email ya está verificado");
        }

        createVerificationCode(user);
    }

    @Scheduled(cron = "0 */5 * * * ?") // Cada 5 minutos
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
        log.info("Códigos expirados eliminados");
    }
}
