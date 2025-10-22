package clinica.medtech.appointments.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import clinica.medtech.appointments.enums.AppointmentType;
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

    @Schema(description = "Tipo de cita médica (PRESENCIAL o VIRTUAL).", example = "VIRTUAL")
    private AppointmentType type;

    @Schema(description = "Motivo de la cita (opcional).", example = "Consulta de control general.")
    @Size(max = 500, message = "El motivo no puede superar los 500 caracteres.")
    private String reason;

    @Schema(description = "Nueva fecha de la cita (formato: yyyy-MM-dd).", example = "2025-10-22")
    private LocalDate appointmentDate;

    @Schema(description = "Nueva hora de la cita (formato: HH:mm).", example = "11:30")
    private LocalTime appointmentTime;

    @Schema(description = "Nueva duración de la cita en minutos (opcional, entre 15 y 120).", 
            example = "45", defaultValue = "30")
    @Positive(message = "La duración debe ser un número positivo.")
    @Min(value = 15, message = "La duración mínima es de 15 minutos.")
    @Max(value = 120, message = "La duración máxima es de 120 minutos.")
    private Integer duration;

    @Schema(description = "Notas adicionales sobre la cita (opcional).", 
            example = "El paciente solicitó cambio a cita virtual.")
    private String notes;
}