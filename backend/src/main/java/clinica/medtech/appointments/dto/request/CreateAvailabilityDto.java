package clinica.medtech.appointments.dto.request;

import java.time.DayOfWeek;
import java.time.OffsetTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Solicitud para registrar la disponibilidad de un médico en un día específico")
public class CreateAvailabilityDto {
    @NotNull(message = "El ID del doctor es obligatorio")
    @Schema(example = "101", description = "ID del doctor que registra su disponibilidad")
    private Long doctorId;

    @NotNull(message = "El día de la semana es obligatorio")
    @Schema(example = "MONDAY", description = "Día de la semana de la disponibilidad")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Schema(example = "08:00-05:00", description = "Hora de inicio con zona horaria (formato HH:mmXXX)")
    @JsonFormat(pattern = "HH:mmXXX")
    private OffsetTime startTime;

    @NotNull(message = "La hora de fin es obligatoria")
    @Schema(example = "16:00-05:00", description = "Hora de finalización con zona horaria (formato HH:mmXXX)")
    @JsonFormat(pattern = "HH:mmXXX")
    private OffsetTime endTime;

    @Schema(example = "true", description = "Indica si la disponibilidad está activa")
    private Boolean isActive = true;

}
