package clinica.medtech.appointments.dto.request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Schema(description = "Solicitud para actualizar parcialmente una cita médica existente.")
public class AppointmentUpdateRequest {

    @Schema(description = "Identificador único de la cita (opcional, se toma de la URL).", example = "15")
    private Long id;

    @Schema(description = "Identificador del doctor asignado a la cita (opcional).", example = "7")
    private Long doctorId;

    @Schema(description = "Identificador del paciente asociado (opcional).", example = "32")
    private Long patientId;

    @Schema(description = "Motivo de la cita (opcional).", example = "Consulta de seguimiento por control de presión arterial.")
    @Size(max = 500, message = "El motivo no puede superar los 500 caracteres.")
    private String reason;

    @Schema(description = "Nueva fecha y hora de la cita (opcional).", example = "2025-10-22T11:00:00")
    private LocalDateTime appointmentDateTime;

    @Schema(description = "Nueva duración de la cita en minutos (opcional) (entre 15 y 120).", example = "45", defaultValue = "30")
    @Positive(message = "La duración debe ser un número positivo.")
    @Min(value = 15, message = "La duración mínima es de 15 minutos.")
    @Max(value = 120, message = "La duración máxima es de 120 minutos.")
    private Integer duration;
}