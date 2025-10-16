package clinica.medtech.appointments.dto.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Solicitud para consultar disponibilidad de un médico entre fechas específicas")
public class AvailabilityRequestDto {
    @Schema(example = "101", description = "ID del médico a consultar")
    private Long doctorId;

    @Schema(example = "2025-10-20", description = "Fecha de inicio del rango de disponibilidad")
    private LocalDate startDate;

    @Schema(example = "2025-10-25", description = "Fecha de fin del rango de disponibilidad")
    private LocalDate endDate;
}
