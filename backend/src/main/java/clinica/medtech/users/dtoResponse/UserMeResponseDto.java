package clinica.medtech.users.dtoResponse;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeResponseDto {
    private Long id;
    private String email;
    private String name;
    private String lastName;
    private List<String> roles;

    // Datos del paciente
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String address;
    private String bloodType;
    private String city;
    private String country;
    private String zip;

    // Datos del profesional
    private String specialty;
    private String medicalLicense;
    private String biography;
    private BigDecimal consultationFee;

}
