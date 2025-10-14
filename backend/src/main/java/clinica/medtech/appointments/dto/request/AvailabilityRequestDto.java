package clinica.medtech.appointments.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AvailabilityRequestDto {
    private Long doctorId;
    private LocalDate startDate;
    private LocalDate endDate;
}
