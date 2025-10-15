package clinica.medtech.users.dtoRequest;

import clinica.medtech.auth.validation.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatches
public class PatientRequestDto {
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo debe contener letras")
    @Size(min = 3, max = 15, message = "El nombre debe tener entre 3 y 15 caracteres")
    @Schema(description = "Nombre del paciente", example = "Juan Carlos")
    @NotNull(message = "El nombre es obligatorio")
    private String name;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El apellido solo debe contener letras")
    @Size(min = 3, max = 15, message = "El apellido debe tener entre 3 y 15 caracteres")
    @Schema(description = "Apellido del paciente", example = "Perez")
    @NotNull(message = "El apellido es obligatorio")
    private String lastName;

    @Email(message = "Formato de email inválido")
    @Pattern(
            regexp = "^(?:[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}|[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+)$",
            message = "El email debe tener un formato válido (por ejemplo: usuario@dominio.com o usuario@localhost)"
    )
    @Schema(description = "Email del paciente", example = "9k6w5@example.com")
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, 1 minúscula, 1 mayúscula, 1 número y 1 carácter especial"
    )
    @Size(min = 8, max = 15, message = "La contraseña debe tener entre 8 y 15 caracteres")
    @NotNull(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del paciente", example = "Password@123")
    private String password;

    @Schema(description = "Confirmar contraseña del paciente", example = "Password@123")
    private String confirmPassword;


}
