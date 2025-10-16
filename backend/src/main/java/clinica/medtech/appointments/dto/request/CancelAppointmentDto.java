package clinica.medtech.appointments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Solicitud para cancelar una cita médica existente")
public class CancelAppointmentDto {
     @NotNull(message = "El ID de la cita es obligatorio")
    @Schema(example = "501", description = "ID único de la cita a cancelar")
    private Long appointmentId;

    @Size(max = 300, message = "La razón de cancelación no debe superar los 300 caracteres")
    @Schema(example = "El paciente no podrá asistir a la cita", description = "Motivo de la cancelación")
    private String reason;
}
