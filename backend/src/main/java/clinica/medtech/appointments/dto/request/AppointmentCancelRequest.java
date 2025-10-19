package clinica.medtech.appointments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Solicitud para cancelar una cita médica existente.")
public class AppointmentCancelRequest {

    @Schema(description = "Identificador único de la cita que se desea cancelar.", example = "12")
    @NotNull(message = "El ID de la cita es obligatorio.")
    private Long id;

    @Schema(description = "Motivo de la cancelación de la cita.", example = "El paciente no podrá asistir por motivos personales.")
    @NotBlank(message = "Debe especificar un motivo de cancelación.")
    private String cancelReason;
}