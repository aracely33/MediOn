package clinica.medtech.appointments.exception;

public class AppointmentConflictException extends RuntimeException {
    public AppointmentConflictException(String message) {
        super(message);
    }

    public static AppointmentConflictException overlapping(Long doctorId) {
        return new AppointmentConflictException("El doctor con ID " + doctorId + " ya tiene una cita en ese horario.");
    }
}