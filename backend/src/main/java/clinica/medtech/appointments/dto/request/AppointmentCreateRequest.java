package clinica.medtech.appointments.dto.request;



import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud para agendar una nueva cita médica.")
public class AppointmentCreateRequest {

    @Schema(description = "Identificador único del doctor que atenderá la cita.", example = "5")
    @NotNull(message = "El ID del doctor es obligatorio.")
    private Long doctorId;

    @Schema(description = "Identificador único del paciente (opcional).", example = "23")
    private Long patientId;

    @Schema(description = "Motivo o razón principal de la cita.", example = "Dolor de cabeza persistente y mareos.")
    @NotBlank(message = "El motivo de la cita es obligatorio.")
    @Size(max = 500, message = "El motivo no puede superar los 500 caracteres.")
    private String reason;

    @Schema(description = "Fecha y hora programada para la cita médica.", example = "2025-10-20T09:30:00")
    @NotNull(message = "La fecha y hora de la cita son obligatorias.")
    private LocalDateTime appointmentDateTime;

    @Schema(description = "Duración estimada de la cita en minutos (entre 15 y 120).", example = "30", defaultValue = "30")
    @Positive(message = "La duración debe ser un número positivo.")
    @Min(value = 15, message = "La duración mínima es de 15 minutos.")
    @Max(value = 120, message = "La duración máxima es de 120 minutos.")
    private Integer duration = 30;
}