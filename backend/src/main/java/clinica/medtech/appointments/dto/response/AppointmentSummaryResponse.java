package clinica.medtech.appointments.dto.response;

import java.time.LocalDateTime;
import clinica.medtech.appointments.enums.AppointmentStatus;
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
public class AppointmentSummaryResponse {

    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDateTime appointmentDateTime;
    private LocalDateTime endDateTime;
    private AppointmentStatus status;
}