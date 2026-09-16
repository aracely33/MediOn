package clinica.medtech.users.service;

import clinica.medtech.users.entities.PatientModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class FhirPatientService {

    private final WebClient webClient;

    public FhirPatientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://hapi.fhir.org/baseR4").build();
    }

    public String createPatientOnFhir(PatientModel patient) {
        Map<String, Object> fhirPatient = Map.of(
                "resourceType", "Patient",
                "text", Map.of(
                        "status", "generated",
                        "div", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><table class=\"hapiPropertyTable\"><tbody/></table></div>"
                ),
                "name", List.of(Map.of(
                        "use", "official",
                        "family", patient.getLastName(),
                        "given", List.of(patient.getName())
                )),
                "telecom", List.of(Map.of(
                        "system", "email",
                        "value", patient.getEmail()
                ))
        );

        return webClient.post()
                .uri("/Patient")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(fhirPatient)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }



    public String getPatientByEmail(String email) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/Patient")
                        .queryParam("email", email)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // bloqueamos solo por simplicidad
    }
}
