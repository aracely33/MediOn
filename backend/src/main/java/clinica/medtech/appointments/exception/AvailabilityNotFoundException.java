package clinica.medtech.appointments.exception;

public class AvailabilityNotFoundException extends RuntimeException{
    public AvailabilityNotFoundException(String message) {
        super(message);
    }
}
