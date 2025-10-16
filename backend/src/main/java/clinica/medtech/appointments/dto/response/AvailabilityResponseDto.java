package clinica.medtech.appointments.dto.response;

import java.time.DayOfWeek;
import java.time.OffsetTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AvailabilityResponseDto {
    private Long id;
    private Long doctorId;
    private DayOfWeek dayOfWeek;
    private OffsetTime startTime;
    private OffsetTime endTime;
    private Boolean isActive;
}
