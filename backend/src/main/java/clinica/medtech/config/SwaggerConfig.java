package clinica.medtech.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "API de Clínica MedTech",
                description = "API para administración de telemedicina y pacientes",
                termsOfService = "www.clinica-medtech.com",
                version = "1.0.0",
                contact = @Contact(
                        name = "Soporte MedTech",
                        url = "www.medtech-terapia.com/contacto/",
                        email = "soporte@medtech-terapia.com"
                ),
                license = @License(
                        name = "Licencia Clínica MedTech",
                        url = "www.medtech-terapia.com/licencia/",
                        identifier = "Propietaria"
                )
        ),
        servers = {
                @Server(
                        description = "Entorno Local",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Producción",
                        url = "https://clinica-shsg.onrender.com"
                )
        },
        security = @SecurityRequirement(
                name = "autenticacionTerapeuta"
        )
)
@SecurityScheme(
        name = "autenticacionTerapeuta",
        description = "Token JWT para acceso de terapeutas y pacientes",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
