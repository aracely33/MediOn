package clinica.medtech.appointments.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CancelAppointmentDto {
    @NotNull(message = "El ID de la cita es obligatorio")
    private Long appointmentId;

    @Size(max = 300, message = "La razón de cancelación no debe superar los 300 caracteres")
    private String reason;
}
