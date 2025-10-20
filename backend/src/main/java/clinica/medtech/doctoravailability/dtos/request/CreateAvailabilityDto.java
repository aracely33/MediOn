package clinica.medtech.doctoravailability.dtos.request;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Solicitud para registrar la disponibilidad de un médico en un día específico.")
public class CreateAvailabilityDto {

    @NotNull(message = "El ID del doctor es obligatorio")
    @Schema(
        example = "101",
        description = "ID del doctor que registra su disponibilidad. Debe existir previamente en la base de datos."
    )
    private Long doctorId;

    @NotNull(message = "El día de la semana es obligatorio")
    @Schema(
        example = "MONDAY",
        description = "Día de la semana de la disponibilidad. Valores válidos: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY.",
        allowableValues = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}
    )
    private DayOfWeek dayOfWeek;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Schema(
        example = "08:00",
        description = "Hora de inicio de la disponibilidad (formato 24h, HH:mm). Ejemplo: 08:00 → 8 de la mañana."
    )
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "La hora de fin es obligatoria")
    @Schema(
        example = "16:00",
        description = "Hora de finalización de la disponibilidad (formato 24h, HH:mm). Ejemplo: 16:00 → 4 de la tarde."
    )
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Schema(
        example = "true",
        description = "Indica si la disponibilidad está activa. Por defecto es true.",
        allowableValues = {"true", "false"},
        defaultValue = "true"
    )
    private Boolean isActive = true;
}
