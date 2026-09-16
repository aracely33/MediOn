package clinica.medtech.calendar.service;

import clinica.medtech.appointments.entity.Appointment;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
import java.util.Date;

@Service
public class GoogleCalendarService {

    @Autowired
    private Calendar googleCalendar;

    // En CALENDAR_ID se coloca el id del calendario creado para que ahí se agreguen los eventos (citas)
    private static final String CALENDAR_ID = "";

    public void createEventWithAppointment(Appointment appointment, LocalDate appointmentDate, LocalTime appointmentTime) {
        System.out.println("Creando evento en Google Calendar para cita ID: " + appointment.getId());

        if (googleCalendar == null) {
            System.err.println("❌ El cliente de Google Calendar no está inicializado.");
            return;
        }

        if (CALENDAR_ID == null || CALENDAR_ID.isBlank()) {
            System.err.println("❌ CALENDAR_ID no configurado, evento no se creará.");
            return;
        }

        try {

            LocalDateTime startDateTime = LocalDateTime.of(appointmentDate, appointmentTime);
            LocalDateTime endDateTime = startDateTime.plusMinutes(30);

            DateTime startGoogleDateTime = localDateTimeToGoogleDateTime(startDateTime);
            DateTime endGoogleDateTime = localDateTimeToGoogleDateTime(endDateTime);

            Event event = new Event()
                    .setSummary("Cita médica - Aleatorio")
                    .setDescription("Cita " + appointment.getType().name().toLowerCase() +
                            "con el Dr. Anónimo")
                    .setStart(new EventDateTime()
                            .setDateTime(startGoogleDateTime)
                            .setTimeZone(ZoneId.systemDefault().getId()))
                    .setEnd(new EventDateTime()
                            .setDateTime(endGoogleDateTime)
                            .setTimeZone(ZoneId.systemDefault().getId()));

            Event createdEvent = googleCalendar.events().insert(CALENDAR_ID, event).execute();
            System.out.println("Evento creado");
            System.out.println("✅ Evento creado con ID: " + createdEvent.getId());
            System.out.println("📅 Link al evento: " + createdEvent.getHtmlLink());

        } catch (IOException e) {
            System.err.println("Error al crear un evento en Google Calendar" + e.getMessage());
            e.printStackTrace();
        }

    }

    private DateTime localDateTimeToGoogleDateTime(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return new DateTime(Date.from(instant));
    }

}
