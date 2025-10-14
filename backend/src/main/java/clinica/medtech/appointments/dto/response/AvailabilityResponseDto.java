package clinica.medtech.appointments.dto.response;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AvailabilityResponseDto {
    private Long id;
    private Long doctorId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isActive;
}
