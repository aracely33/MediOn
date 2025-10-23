package clinica.medtech.doctoravailability.dtos.request;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Solicitud para actualizar parcialmente una disponibilidad médica. Todos los campos son opcionales.")
public class UpdateAvailabilityDto {

    @Schema(
        description = "Día de la semana (opcional). Valores válidos: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY.",
        example = "MONDAY",
        allowableValues = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}
    )
    private DayOfWeek dayOfWeek;

    @Schema(
        description = "Hora de inicio (opcional). Formato 24h (HH:mm). Ejemplo: 08:00 → 8 de la mañana.",
        example = "08:00"
    )
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Schema(
        description = "Hora de fin (opcional). Formato 24h (HH:mm). Ejemplo: 16:00 → 4 de la tarde.",
        example = "16:00"
    )
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Schema(
        description = "Indica si la disponibilidad está activa (opcional). Si se marca en 'false', se desactiva.",
        example = "true",
        allowableValues = {"true", "false"},
        defaultValue = "true"
    )
    private Boolean isActive;
}
