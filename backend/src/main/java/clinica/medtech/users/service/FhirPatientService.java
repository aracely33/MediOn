package clinica.medtech.users.service;

import clinica.medtech.users.entities.PatientModel;
import clinica.medtech.users.repository.PatientRepository;
import clinica.medtech.users.mapper.FhirPatientMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class FhirPatientService {

    private final WebClient webClient;
    private final PatientRepository patientRepository;
    private final ObjectMapper objectMapper;
    private final FhirPatientMapper fhirPatientMapper;

    public FhirPatientService(WebClient.Builder webClientBuilder, PatientRepository patientRepository, FhirPatientMapper fhirPatientMapper) {
        this.webClient = webClientBuilder.baseUrl("https://hapi.fhir.org/baseR4").build();
        this.patientRepository = patientRepository;
        this.objectMapper = new ObjectMapper();
        this.fhirPatientMapper = fhirPatientMapper;
    }

    public String createPatientOnFhir(PatientModel patient) {
        // Usar el mapper para crear la estructura FHIR completa
        Map<String, Object> fhirPatient = fhirPatientMapper.toFhirPatient(patient);

        String response = webClient.post()
                .uri("/Patient")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(fhirPatient)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Extraer el ID FHIR de la respuesta y actualizar el paciente
        try {
            JsonNode responseNode = objectMapper.readTree(response);
            String fhirId = responseNode.get("id").asText();
            
            // Actualizar el fhirId del paciente
            patient.setFhirId(fhirId);
            patientRepository.save(patient);
            
        } catch (Exception e) {
            // Log del error pero continuamos con la respuesta original
            System.err.println("Error extracting FHIR ID: " + e.getMessage());
        }

        return response;
    }

    /**
     * Crea un paciente FHIR usando el mapper completo y retorna la respuesta estructurada.
     * Este método utiliza todas las propiedades disponibles del PatientModel para crear
     * un recurso FHIR Patient completo con todos los campos mapeados correctamente.
     * 
     * @param patient El modelo de paciente con todos los datos disponibles
     * @return La respuesta JSON completa del servidor FHIR con el recurso Patient creado
     */
    public String createCompleteFhirPatient(PatientModel patient) {
        // Usar el mapper para construir la estructura FHIR completa con todos los campos
        Map<String, Object> completeFhirPatient = fhirPatientMapper.toFhirPatient(patient);
        
        // Enviar al servidor FHIR
        String response = webClient.post()
                .uri("/Patient")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(completeFhirPatient)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Procesar la respuesta y actualizar el fhirId del paciente
        try {
            JsonNode responseNode = objectMapper.readTree(response);
            if (responseNode.has("id")) {
                String fhirId = responseNode.get("id").asText();
                patient.setFhirId(fhirId);
                patientRepository.save(patient);
            }
        } catch (Exception e) {
            System.err.println("Error processing FHIR response: " + e.getMessage());
        }

        return response;
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

    public String getPatientByIDFhir(String fhirId) {
        return webClient.get()
                .uri("/Patient/{id}", fhirId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // bloqueamos solo por simplicidad
    }
}
