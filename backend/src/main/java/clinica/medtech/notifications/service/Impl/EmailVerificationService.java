package clinica.medtech.notifications.service.Impl;

import clinica.medtech.notifications.entities.EmailVerificationToken;
import clinica.medtech.notifications.repository.EmailVerificationTokenRepository;
import clinica.medtech.notifications.service.EmailService;
import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.entities.UserModel;
import clinica.medtech.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

        String verificationCode = generateVerificationCode();
        EmailVerificationToken token = EmailVerificationToken.builder()
                .verificationCode(verificationCode)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .createdDate(LocalDateTime.now())
                .build();
        tokenRepository.save(token);

        sendVerificationEmail(user, verificationCode);
        log.info("Código de verificación enviado a {}", user.getEmail());
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    private void sendVerificationEmail(UserModel user, String verificationCode) {
        emailService.sendVerificationEmail(user.getEmail(), user.getName(), verificationCode);
    }

    @Transactional
    public Optional<UserModel> verifyEmail(String verificationCode) {
        EmailVerificationToken token = tokenRepository.findByVerificationCode(verificationCode)
                .orElse(null);

        if (token == null || token.getUsed() || token.isExpired()) {
            return Optional.empty();
        }

        UserModel user = token.getUser();
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        token.setUsed(true);
        tokenRepository.save(token);

        return Optional.of(user);
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


}
