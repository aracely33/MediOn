package clinica.medtech.appointments.dto.request;

import java.time.OffsetDateTime;


import com.fasterxml.jackson.annotation.JsonFormat;

import clinica.medtech.appointments.enums.AppointmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Solicitud para crear una nueva cita médica")
public class CreateAppointmentDto {
    @NotNull(message = "La fecha de la cita es obligatoria")
    @Future(message = "La fecha debe ser futura")
    @Schema(example = "2025-11-16T20:26-05:00", description = "Fecha y hora de la cita en formato ISO 8601 con zona horaria")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mmXXX")
    private OffsetDateTime appointmentDateTime;

    @NotNull(message = "El tipo de cita es obligatorio")
    @Schema(example = "PRESENCIAL", description = "Tipo de cita (PRESENCIAL o VIRTUAL)")
    private AppointmentType type;

    @NotNull(message = "El ID del médico es obligatorio")
    @Schema(example = "101", description = "ID del doctor asignado")
    private Long doctorId;

    @NotBlank(message = "El motivo de la cita es obligatorio")
    @Size(max = 500, message = "El motivo no debe superar los 500 caracteres")
    @Schema(example = "Chequeo general y análisis de laboratorio", description = "Motivo principal de la cita")
    private String reason;

    @Schema(example = "El paciente solicitó resultados impresos", description = "Notas adicionales de la cita")
    private String notes;

    @Min(15)
    @Max(120)
    @Schema(example = "30", description = "Duración estimada de la cita en minutos (15–120)")
    private Integer duration = 30;
}
