package clinica.medtech.appointments.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import clinica.medtech.appointments.enums.AppointmentType;
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
@Schema(description = "Solicitud para crear una nueva cita médica.")
public class AppointmentCreateRequest {

    @NotNull(message = "El ID del paciente es obligatorio.")
    private Long patientId;

    @NotNull(message = "El ID del doctor es obligatorio.")
    private Long doctorId;

    @NotNull(message = "El tipo de cita es obligatorio.")
    private AppointmentType type; // PRESENCIAL o VIRTUAL

    @Schema(description = "Fecha de la cita (formato: yyyy-MM-dd)", example = "2025-10-18")
    @NotNull(message = "La fecha de la cita es obligatoria.")
    private LocalDate appointmentDate;

    @Schema(description = "Hora de inicio de la cita (formato: HH:mm)", example = "14:30")
    @NotNull(message = "La hora de la cita es obligatoria.")
    private LocalTime appointmentTime;

    @Positive(message = "La duración debe ser positiva.")
    @Min(value = 15, message = "La duración mínima es de 15 minutos.")
    @Max(value = 120, message = "La duración máxima es de 120 minutos.")
    private Integer duration = 30;

    @NotBlank(message = "El motivo de la cita es obligatorio.")
    @Size(max = 500, message = "El motivo no puede superar los 500 caracteres.")
    private String reason;

    @Schema(description = "Notas opcionales sobre la cita", example = "Paciente solicita cita virtual por telemedicina.")
    private String notes;
}