package clinica.medtech.users.dtoResponse;

import lombok.*;

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
}
