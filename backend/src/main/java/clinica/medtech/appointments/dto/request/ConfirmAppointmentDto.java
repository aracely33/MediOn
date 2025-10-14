package clinica.medtech.appointments.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfirmAppointmentDto {
    @NotNull(message = "El ID de la cita es obligatorio")
    private Long appointmentId;

    private String confirmationNotes;
}
