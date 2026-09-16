package clinica.medtech.exceptions;

import lombok.*;

import java.time.Instant;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
    private List<String> details;
    private Instant timestamp;
    private String path;
}
