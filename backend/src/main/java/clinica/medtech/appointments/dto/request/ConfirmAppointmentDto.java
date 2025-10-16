package clinica.medtech.appointments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Solicitud para confirmar una cita médica")
public class ConfirmAppointmentDto {
    @NotNull(message = "El ID de la cita es obligatorio")
    @Schema(example = "501", description = "ID único de la cita a confirmar")
    private Long appointmentId;

    @Schema(example = "Confirmada por el paciente vía telefónica", description = "Notas opcionales de confirmación")
    private String confirmationNotes;
}
