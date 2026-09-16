package clinica.medtech.notifications.service;

//Helloword
public interface EmailService {

    void sendWelcomeEmail(String userEmail, String userName);

    void sendVerificationEmail(String userEmail, String userName, String verificationCode);

}
