package clinica.medtech.appointments.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import clinica.medtech.appointments.enums.AppointmentType;
import io.swagger.v3.oas.annotations.media.Schema;
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
    name = "AppointmentUpdateRequest",
    description = "Solicitud para actualizar parcialmente una cita médica existente."
)
public class AppointmentUpdateRequest {

    @Schema(description = "Nuevo ID del doctor", example = "3")
    private Long doctorId;

    @Schema(description = "Nuevo tipo de cita", example = "VIRTUAL")
    private AppointmentType type;

    @Schema(description = "Nuevo motivo de la cita", example = "Cambio de tratamiento")
    @Size(max = 500)
    private String reason;

    @Schema(description = "Nueva fecha de la cita (formato: yyyy-MM-dd)", example = "2025-11-19")
    private LocalDate appointmentDate;

    @Schema(description = "Nueva hora de la cita (formato: HH:mm)", example = "15:00")
    private LocalTime appointmentTime;

    @Schema(description = "Notas adicionales o comentarios", example = "Reprogramada por disponibilidad del paciente")
    private String notes;
}
