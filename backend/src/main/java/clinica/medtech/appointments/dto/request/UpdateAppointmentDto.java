package clinica.medtech.appointments.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAppointmentDto {
    @NotNull
    private Long id;

    @Future(message = "La fecha debe ser futura")
    private LocalDateTime appointmentDateTime;

    @Size(max = 500)
    private String reason;

    private String notes;

    @Min(15)
    @Max(120)
    private Integer duration;
}
