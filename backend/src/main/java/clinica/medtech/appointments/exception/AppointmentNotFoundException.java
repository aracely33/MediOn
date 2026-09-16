package clinica.medtech.appointments.exception;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(Long id) {
        super("No se encontró la cita con ID: " + id);
    }
}