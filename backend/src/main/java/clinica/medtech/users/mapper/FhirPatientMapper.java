package clinica.medtech.users.mapper;

import clinica.medtech.users.entities.PatientModel;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FhirPatientMapper {

    /**
     * Construye un Map representando el recurso FHIR Patient mínimo necesario para POST.
     */
    public Map<String, Object> toFhirPatient(PatientModel p) {
        Map<String, Object> patient = new LinkedHashMap<>();
        patient.put("resourceType", "Patient");

        // identifier (use patient id if available, otherwise email or UUID)
        String idValue = p.getId() != null ? String.valueOf(p.getId()) :
                (p.getEmail() != null && !p.getEmail().isBlank() ? p.getEmail() : UUID.randomUUID().toString());
        Map<String, Object> identifier = Map.of(
                "system", "urn:medtech:patient-id",
                "value", idValue
        );
        patient.put("identifier", List.of(identifier));

        // name
        Map<String, Object> name = new LinkedHashMap<>();
        if (p.getLastName() != null) name.put("family", p.getLastName());
        if (p.getName() != null) name.put("given", List.of(p.getName()));
        patient.put("name", List.of(name));

        // telecom: email / phone
        List<Map<String, Object>> telecom = new ArrayList<>();
        if (p.getEmail() != null && !p.getEmail().isBlank()) {
            telecom.add(Map.of("system", "email", "value", p.getEmail(), "use", "home"));
        }
        if (p.getPhone() != null && !p.getPhone().isBlank()) {
            telecom.add(Map.of("system", "phone", "value", p.getPhone(), "use", "mobile"));
        }
        if (!telecom.isEmpty()) patient.put("telecom", telecom);

        // gender mapping (normalize)
        if (p.getGender() != null && !p.getGender().isBlank()) {
            String g = p.getGender().trim().toLowerCase();
            if (g.startsWith("m")) patient.put("gender", "male");
            else if (g.startsWith("f")) patient.put("gender", "female");
            else if (g.contains("other")) patient.put("gender", "other");
            else patient.put("gender", "unknown");
        }

        // birthDate
        if (p.getBirthDate() != null) {
            patient.put("birthDate", p.getBirthDate().toString()); // ISO yyyy-MM-dd
        }

        // address (minimal)
        if ((p.getAddress() != null && !p.getAddress().isBlank()) ||
                (p.getCity() != null && !p.getCity().isBlank()) ||
                (p.getCountry() != null && !p.getCountry().isBlank()) ||
                (p.getZip() != null && !p.getZip().isBlank())) {

            Map<String, Object> address = new LinkedHashMap<>();
            if (p.getAddress() != null && !p.getAddress().isBlank()) address.put("line", List.of(p.getAddress()));
            if (p.getCity() != null) address.put("city", p.getCity());
            if (p.getCountry() != null) address.put("country", p.getCountry());
            if (p.getZip() != null) address.put("postalCode", p.getZip());
            patient.put("address", List.of(address));
        }

        // active flag
        patient.put("active", true);

        return patient;
    }
}