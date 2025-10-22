package clinica.medtech.notifications.service.Impl;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import clinica.medtech.notifications.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import com.sendgrid.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailImpl implements EmailService {

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.welcome.subject}")
    private String welcomeSubject;

    @Value("${app.email.verification.subject}")
    private String verificationSubject;

    @Value("${app.sendgrid.api-key}")
    private String sendGridApiKey;

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

    private void sendEmail(String to, String subject, String htmlContent) throws IOException {
        Email from = new Email(fromEmail);
        Email recipient = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, recipient, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                log.debug("Email enviado exitosamente a: {}", to);
            } else {
                log.error("Error de SendGrid: {} - {}", response.getStatusCode(), response.getBody());
            }

        } catch (IOException e) {
            log.error("Error enviando email a: {}", to, e);
            throw e;
        }
    }

    private String loadWelcomeTemplate(String userName) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/welcome-email.html");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return template.replace("{userName}", userName);
    }

    private String loadVerificationTemplate(String userName, String verificationCode) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/verification-email.html");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return template
                .replace("{userName}", userName)
                .replace("{verificationCode}", verificationCode);
    }
}