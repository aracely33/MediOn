package clinica.medtech.appointments.exception;

public class InvalidAppointmentStateException extends RuntimeException {
    public InvalidAppointmentStateException(String message) {
        super(message);
    }
}