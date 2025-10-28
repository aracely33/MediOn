package clinica.medtech.users.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean emailVerified;

    @Column
    private LocalDateTime emailVerifiedAt;

    @CreationTimestamp
    @Column(name = "register_date", updatable = false)
    private LocalDate registerDate;

    @UpdateTimestamp
    @Column(name = "last_login")
    private LocalDate lastLogin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleModel> roles = new HashSet<>();

    @Column(name = "suspension_end")
    private LocalDateTime suspensionEnd;

    @PrePersist
    @PreUpdate
    public void normalizeEmail() {
        if (this.email != null) {
            this.email = this.email.trim().toLowerCase();
        }
    }

    public boolean isEnabled() {
        return suspensionEnd == null || suspensionEnd.isBefore(LocalDateTime.now());
    }


    public boolean isEmailVerified() {
        return Boolean.TRUE.equals(this.emailVerified);
    }

    public void verifyEmail() {
        this.emailVerified = true;
        this.emailVerifiedAt = LocalDateTime.now();
    }
}
