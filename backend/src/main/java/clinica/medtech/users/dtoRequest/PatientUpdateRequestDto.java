package clinica.medtech.users.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientUpdateRequestDto {
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo debe contener letras")
    @Size(min = 3, max = 15, message = "El nombre debe tener entre 3 y 15 caracteres")
    @Schema(description = "Nombre del paciente", example = "Juan Carlos")
    private String name;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El apellido solo debe contener letras")
    @Size(min = 3, max = 15, message = "El apellido debe tener entre 3 y 15 caracteres")
    @Schema(description = "Apellido del paciente", example = "Perez")
    private String lastName;

    @Email(message = "Formato de email inválido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[A-Za-z]{2,}([A-Za-z]{2,})?$",
            message = "El email debe tener un dominio válido, como .com o .com.ar"
    )
    @Schema(description = "Email del paciente", example = "9k6w5@example.com")
    private String email;

    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    @Schema(description = "La fecha de nacimiento del paciente", example = "2000-01-01")
    private LocalDate birthDate;

    @Schema(description = "Genero del paciente", example = "M")
    private String gender;

    @Schema(description = "Direccion del paciente", example = "Calle 123")
    private String address;

    @Pattern(regexp = "^[0-9]{10}$", message = "El telefono debe tener 10 digitos")
    @Schema(description = "Telefono del paciente", example = "1234567890")
    private String phone;

    @Pattern(
            regexp = "^(A|B|AB|O)[+-]$",
            message = "El tipo de sangre debe ser uno de: A+, A-, B+, B-, AB+, AB-, O+, O-"
    )
    @Schema(description = "Tipo de sangre del paciente", example = "A+")
    private String bloodType;

}
