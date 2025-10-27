package clinica.medtech.appointments.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import clinica.medtech.appointments.enums.AppointmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "AppointmentCreateRequest",
    description = "Solicitud para crear una nueva cita médica."
)
public class AppointmentCreateRequest {

    @Schema(description = "ID del doctor con quien se agenda la cita", example = "5", required = true)
    @NotNull(message = "El ID del doctor es obligatorio.")
    private Long doctorId;

    @Schema(description = "Tipo de cita (PRESENCIAL o VIRTUAL)", example = "PRESENCIAL", required = true)
    @NotNull(message = "El tipo de cita es obligatorio.")
    private AppointmentType type;

    @Schema(description = "Fecha de la cita (formato: yyyy-MM-dd)", example = "2025-11-18", required = true)
    @NotNull(message = "La fecha de la cita es obligatoria.")
    private LocalDate appointmentDate;

    @Schema(description = "Hora de inicio (formato: HH:mm)", example = "14:30", required = true)
    @NotNull(message = "La hora de la cita es obligatoria.")
    private LocalTime appointmentTime;

    @Schema(description = "Motivo de la cita", example = "Dolor de cabeza persistente", required = true)
    @NotBlank(message = "El motivo de la cita es obligatorio.")
    @Size(max = 500, message = "El motivo no puede superar los 500 caracteres.")
    private String reason;

    @Schema(description = "Notas adicionales sobre la cita", example = "Paciente solicita atención virtual.")
    private String notes;
}