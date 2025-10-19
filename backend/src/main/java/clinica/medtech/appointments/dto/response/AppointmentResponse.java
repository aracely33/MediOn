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
public class AppointmentResponse {

    private Long id;
    private Long doctorId;
    private Long patientId;
    private String reason;
    private LocalDateTime appointmentDateTime;
    private LocalDateTime endDateTime;
    private Integer duration;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}