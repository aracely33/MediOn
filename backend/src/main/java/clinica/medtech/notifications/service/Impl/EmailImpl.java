package clinica.medtech.notifications.service.Impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import clinica.medtech.notifications.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.welcome.subject}")
    private String welcomeSubject;

    @Value("${app.email.verification.subject}")
    private String verificationSubject;

    @Override
    public void sendWelcomeEmail(String userEmail, String userName) {
        try {
            String htmlContent = loadWelcomeTemplate(userName);
            sendEmail(userEmail, welcomeSubject, htmlContent);
            log.info("Email de bienvenida enviado exitosamente a: {}", userEmail);
        } catch (Exception e) {
            log.error("Error enviando email de bienvenida a: {}", userEmail, e);
        }
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.debug("Email enviado exitosamente a: {}", to);

        } catch (Exception e) {
            log.error("Error enviando email a: {}", to, e);
        }
    }

    private String loadWelcomeTemplate(String userName) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/welcome-email.html");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        return template.replace("{userName}", userName);
    }

    @Override
    public void sendVerificationEmail(String userEmail, String userName, String verificationCode) {
        try {
            String htmlContent = loadVerificationTemplate(userName, verificationCode);
            sendEmail(userEmail, verificationSubject, htmlContent);
            log.info("Código de verificación enviado exitosamente a: {}", userEmail);
        } catch (Exception e) {
            log.error("Error enviando código de verificación a: {}", userEmail, e);
        }
    }

    private String loadVerificationTemplate(String userName, String verificationCode) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/verification-email.html");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        return template.replace("{userName}", userName)
                .replace("{verificationCode}", verificationCode);
    }
}