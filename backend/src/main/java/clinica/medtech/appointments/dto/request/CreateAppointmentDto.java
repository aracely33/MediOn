package clinica.medtech.appointments.dto.request;

import java.time.LocalDateTime;

import clinica.medtech.appointments.enums.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAppointmentDto {
    @NotNull(message = "La fecha de la cita es obligatoria")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "El tipo de cita es obligatorio")
    private AppointmentType type;

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long patientId;

    @NotNull(message = "El ID del médico es obligatorio")
    private Long doctorId;

    @NotBlank(message = "El motivo de la cita es obligatorio")
    @Size(max = 500, message = "El motivo no debe superar los 500 caracteres")
    private String reason;

    private String notes;

    @Min(15)
    @Max(120)
    private Integer duration = 30;
}
