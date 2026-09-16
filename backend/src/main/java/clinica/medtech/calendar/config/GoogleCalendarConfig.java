package clinica.medtech.calendar.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleCalendarConfig {

    private static final String APPLICATION_NAME = "Spring Boot MedTech with Google Calendar";

    public static Calendar getCalendarService() throws GeneralSecurityException, Exception {

        InputStream credentialStream = GoogleCalendarConfig.class.getResourceAsStream("/google-credentials/calendar-credentials.json");

        GoogleCredential credential = GoogleCredential.fromStream(credentialStream)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
        ).setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Bean
    public Calendar googleCalendar() throws IOException {
        System.out.println("🟢 Inicializando cliente de Google Calendar...");
        HttpTransport httpTransport = new NetHttpTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        String credentialsPath = "src/main/resources/google-credentials/calendar-credentials.json";

        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream(credentialsPath))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/calendar"));

        System.out.println("🔐 Credenciales cargadas correctamente para cliente: " + credential.getServiceAccountId());

        return new Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Google Calendar Appointment")
                .build();
    }

}
