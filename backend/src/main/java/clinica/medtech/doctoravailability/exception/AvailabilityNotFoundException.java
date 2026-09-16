package clinica.medtech.doctoravailability.exception;

public class AvailabilityNotFoundException extends RuntimeException{
    public AvailabilityNotFoundException(String message) {
        super(message);
    }
}
