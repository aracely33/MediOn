package clinica.medtech.appointments.dto.request;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAppointmentDto {
    @Schema(hidden = true)
    private Long id;

    @Future(message = "La fecha debe ser futura")
    @Schema(example = "2025-11-16T20:26-05:00", description = "Nueva fecha y hora de la cita (si aplica)")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mmXXX")
    private OffsetDateTime appointmentDateTime;

    @Size(max = 500, message = "El motivo no debe superar los 500 caracteres")
    @Schema(example = "Cambio de hora por disponibilidad del paciente", description = "Nuevo motivo o descripción de la cita")
    private String reason;

    @Schema(example = "Agregar resultados de laboratorio previos", description = "Notas adicionales de actualización")
    private String notes;

    @Min(15)
    @Max(120)
    @Schema(example = "45", description = "Nueva duración de la cita en minutos (15-120)")
    private Integer duration;
}
