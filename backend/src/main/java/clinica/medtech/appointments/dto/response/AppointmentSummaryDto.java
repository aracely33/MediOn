package clinica.medtech.appointments.dto.response;

import java.time.LocalDateTime;

import clinica.medtech.appointments.enums.AppointmentStatus;
import clinica.medtech.appointments.enums.AppointmentType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppointmentSummaryDto {
    private Long id;
    private LocalDateTime appointmentDateTime;
    private AppointmentType type;
    private AppointmentStatus status;
    private String reason;
}
