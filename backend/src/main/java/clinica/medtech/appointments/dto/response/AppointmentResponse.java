package clinica.medtech.appointments.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


import clinica.medtech.appointments.enums.AppointmentStatus;
import clinica.medtech.appointments.enums.AppointmentType;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Respuesta detallada de una cita médica registrada en el sistema.")
public class AppointmentResponse {

    @Schema(description = "Identificador único de la cita.", example = "45")
    private Long id;

    @Schema(description = "Identificador del doctor asignado.", example = "7")
    private Long doctorId;

    @Schema(description = "Identificador del paciente asociado.", example = "21")
    private Long patientId;

    @Schema(description = "Tipo de cita (PRESENCIAL o VIRTUAL).", example = "VIRTUAL")
    private AppointmentType type;

    @Schema(description = "Estado actual de la cita.", example = "PENDIENTE")
    private AppointmentStatus status;

    @Schema(description = "Fecha programada de la cita.", example = "2025-11-12")
    private LocalDate appointmentDate;

    @Schema(description = "Hora de inicio de la cita (hora local).", example = "10:30")
    private LocalTime appointmentTime;

    @Schema(description = "Hora de finalización de la cita (calculada automáticamente).", example = "11:00")
    private LocalDateTime appointmentEndDateTime;

    @Schema(description = "Duración de la cita en minutos.", example = "30")
    private Integer duration;

    @Schema(description = "Motivo de la cita.", example = "Control de presión arterial.")
    private String reason;

    @Schema(description = "Notas adicionales o comentarios.", example = "El paciente pidió confirmar un día antes.")
    private String notes;

    @Schema(description = "Fecha de creación del registro.", example = "2025-10-20T09:15:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de la última actualización.", example = "2025-10-20T09:30:00")
    private LocalDateTime updatedAt;
}