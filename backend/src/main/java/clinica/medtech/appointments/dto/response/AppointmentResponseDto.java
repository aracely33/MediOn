package clinica.medtech.appointments.dto.response;

import java.time.OffsetDateTime;

import clinica.medtech.appointments.enums.AppointmentStatus;
import clinica.medtech.appointments.enums.AppointmentType;
import lombok.Data;

@Data
public class AppointmentResponseDto {
    private Long id;
    private OffsetDateTime appointmentDateTime;
    private Integer duration;
    private AppointmentType type;
    private AppointmentStatus status;
    private String reason;
    private String notes;
    //private Long patientId;
    private Long doctorId;
    //private String videoConferenceUrl;
    private OffsetDateTime createdAt;
}
