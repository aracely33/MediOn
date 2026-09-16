package clinica.medtech.notifications.repository;

import clinica.medtech.notifications.entities.EmailVerificationToken;
import clinica.medtech.users.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByVerificationCode(String verificationCode);

    Optional<EmailVerificationToken> findByUser(UserModel user);

    void deleteByExpiryDateBefore(LocalDateTime now);

    void deleteByUser(UserModel user);
}