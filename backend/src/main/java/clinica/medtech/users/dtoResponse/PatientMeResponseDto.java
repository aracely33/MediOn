package clinica.medtech.users.dtoResponse;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientMeResponseDto {
    private Long id;
    private String email;
    private String name;
    private String lastName;
    private List<String> roles;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String phone;
    private String bloodType;
}