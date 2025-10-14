package clinica.medtech.users.dtoResponse;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionalResponseDto {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String medicalLicense;
    private String specialty;
    private String biography;
    private BigDecimal consultationFee;
}
