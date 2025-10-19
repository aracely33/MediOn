package clinica.medtech.appointments.dto.request;

import clinica.medtech.appointments.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Solicitud para actualizar el estado de una cita médica.")
public class AppointmentStatusUpdateRequest {

    @Schema(description = "Identificador único de la cita.", example = "10")
    @NotNull(message = "El ID de la cita es obligatorio.")
    private Long id;

    @Schema(description = "Nuevo estado de la cita.", example = "CONFIRMADA",
            allowableValues = {"PENDIENTE", "CONFIRMADA", "CANCELADA", "EN_CURSO", "FINALIZADA"})
    @NotNull(message = "El estado de la cita es obligatorio.")
    private AppointmentStatus status;
}