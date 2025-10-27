package clinica.medtech.exceptions;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import clinica.medtech.appointments.exception.AppointmentConflictException;
import clinica.medtech.appointments.exception.AppointmentNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    /**
     * Manejador de excepciones para errores de validación en los argumentos de los métodos del controlador.
     * Captura instancias de {@link MethodArgumentNotValidException} generadas por anotaciones como {@code @Valid}.
     *
     * @param ex      la excepción lanzada cuando un argumento anotado con {@code @Valid} falla la validación
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 400 (Bad Request) que contiene detalles del error en un objeto {@link ErrorResponse}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        List<String> sanitizedDetails = new ArrayList<>();

        // ✅ Captura errores de campos individuales (FieldErrors)
        List<String> finalSanitizedDetails = sanitizedDetails;
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = sanitizeFieldName(error.getField());
            String message = Optional.ofNullable(error.getDefaultMessage())
                    .orElse("Restricción de validación incumplida");
            finalSanitizedDetails.add(String.format("%s: %s", fieldName, message));
        });

        // ✅ Captura errores a nivel de clase (GlobalErrors, como @PasswordMatches)
        List<String> finalSanitizedDetails1 = sanitizedDetails;
        ex.getBindingResult().getGlobalErrors().forEach(error -> {
            String objectName = sanitizeFieldName(error.getObjectName());
            String message = Optional.ofNullable(error.getDefaultMessage())
                    .orElse("Error de validación en el objeto");
            finalSanitizedDetails1.add(String.format("%s: %s", objectName, message));
        });

        // Ordena por campo para consistencia
        sanitizedDetails = sanitizedDetails.stream()
                .sorted(Comparator.comparing(detail -> detail.split(":")[0]))
                .toList();

        // Si no hay mensajes, deja uno genérico
        List<String> finalDetails = !sanitizedDetails.isEmpty()
                ? sanitizedDetails
                : List.of("Error de validación no especificado");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("VALIDATION-001")
                .message("Errores de validación en la solicitud")
                .details(finalDetails)
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        // 🧠 Logging claro y limpio
        log.warn("Validation Error - Path: {} | IP: {} | Errors: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                finalDetails.stream()
                        .map(detail -> detail.replaceAll("(\r\n|\n|\r)", ""))
                        .collect(Collectors.joining("; ")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Validation-Error", "true")
                .body(errorResponse);
    }


    /**
     * Manejador de excepciones para errores de lectura del cuerpo de la solicitud HTTP.
     * Captura instancias de {@link HttpMessageNotReadableException} cuando el contenido JSON no puede ser deserializado.
     *
     * @param ex      la excepción lanzada al fallar la lectura o deserialización del cuerpo de la solicitud
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 400 (Bad Request) que contiene detalles del error en un objeto {@link ErrorResponse}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            WebRequest request) {

        String rootCauseMessage = extractRootCauseMessage(ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("PARSE-001")
                .message("Cuerpo de solicitud inválido")
                .details(List.of(
                        sanitizeJsonErrorMessage(rootCauseMessage),
                        "Revise la sintaxis JSON y los tipos de datos"
                ))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("JSON Parse Error - Path: {} | IP: {} | ErrorType: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                classifyJsonError(rootCauseMessage));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .header("Accept", "application/json")
                .body(errorResponse);
    }

    /**
     * Manejador de excepciones para parámetros requeridos ausentes en la solicitud.
     * Captura instancias de {@link MissingServletRequestParameterException} cuando falta un parámetro obligatorio.
     *
     * @param ex      la excepción lanzada cuando un parámetro requerido no está presente en la solicitud
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 400 (Bad Request) que contiene detalles del error en un objeto {@link ErrorResponse}
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(
            MissingServletRequestParameterException ex,
            WebRequest request) {

        String paramName = sanitizeParamName(ex.getParameterName());
        String userMessage = "Falta un parámetro requerido en la solicitud";
        String detailMessage = String.format("Parámetro faltante: %s (%s)", paramName, ex.getParameterType());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("PARAM-001")
                .message(userMessage)
                .details(List.of(detailMessage))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Missing Param - Path: {} | Param: {} | Type: {}",
                errorResponse.getPath(),
                paramName,
                ex.getParameterType());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }


    /**
     * Manejador de excepciones para violaciones de restricciones declaradas en parámetros o propiedades.
     * Captura instancias de {@link ConstraintViolationException}, típicamente producidas por validaciones
     * de parámetros de métodos con anotaciones como {@code @NotNull}, {@code @Min}, etc.
     *
     * @param ex      la excepción lanzada cuando una restricción de validación es violada
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 400 (Bad Request) que contiene detalles del error en un objeto {@link ErrorResponse}
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {

        List<String> sanitizedDetails = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String path = sanitizePropertyPath(violation.getPropertyPath().toString());
                    String message = Optional.ofNullable(violation.getMessage())
                            .orElse("Violación de restricción");
                    return String.format("%s: %s", path, message);
                })
                .sorted(Comparator.comparing(detail -> detail.split(":")[0]))
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("VALIDATION-002")
                .message("Violaciones de restricción en los parámetros")
                .details(!sanitizedDetails.isEmpty() ? sanitizedDetails : List.of("Violaciones no especificadas"))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("ConstraintViolation - Path: {} | IP: {} | Violations: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                String.join("; ", sanitizedDetails));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Validation-Error", "true")
                .body(errorResponse);
    }


    /**
     * Manejador de excepciones para solicitudes con métodos HTTP no permitidos.
     * Captura instancias de {@link HttpRequestMethodNotSupportedException} cuando se utiliza un método HTTP
     * que no está soportado por el endpoint solicitado (por ejemplo, enviar un POST donde solo se permite GET).
     *
     * @param ex      la excepción lanzada cuando un método HTTP no es soportado
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 405 (Method Not Allowed) que contiene detalles del error en un objeto {@link ErrorResponse}
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("HTTP-405")
                .message("Método HTTP no permitido")
                .details(List.of("Método no soportado: " + sanitizeErrorMessage(ex.getMethod())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Método no permitido - Path: {} | IP: {} | Método: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMethod());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    /**
     * Manejador de excepciones para solicitudes con recursos no encontrados.
     * Captura instancias de {@link EntityNotFoundException} cuando se intenta acceder a un recurso que no existe.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un recurso que no existe
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 404 (Not Found) que contiene detalles del error en un objeto {@link ErrorResponse}
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("RESOURCE-404")
                .message("El recurso solicitado no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Entity Not Found - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    /**
     * Manejador de excepciones para solicitudes con credenciales inválidas.
     * Captura instancias de {@link BadCredentialsException} cuando se intenta autenticar con credenciales inválidas.
     *
     * @param ex      la excepción lanzada cuando se intenta autenticar con credenciales inválidas
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 401 (Unauthorized) que contiene detalles del error en un objeto {@link ErrorResponse}
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            WebRequest request) {

        String errorDetail = Optional.ofNullable(ex.getMessage())
                .filter(msg -> !msg.isBlank())
                .orElse("Credenciales inválidas (sin detalles adicionales)"); // Mensaje por defecto

        String sanitizedMessage = sanitizeErrorMessage(errorDetail);

        log.warn("Intento de autenticación fallido - IP: {}, User-Agent: {} | Razón: {}",
                request.getHeader("X-Forwarded-For"),
                request.getHeader("User-Agent"),
                sanitizedMessage);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-001")
                .message("Credenciales incorrectas")
                .details(List.of(sanitizedMessage))
                .timestamp(Instant.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    /**
     * Manejador de excepciones para solicitudes con usuarios no encontrados.
     * Captura instancias de {@link UsernameNotFoundException} cuando se intenta acceder a un usuario que no existe.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un usuario que no existe
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 401 (Unauthorized) que contiene detalles del error en un objeto {@link ErrorResponse}
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(
            UsernameNotFoundException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-404")
                .message("El usuario no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Usuario no encontrado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Auth-Error", "true")
                .body(errorResponse);
    }

    @ExceptionHandler(AccountNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotVerified(
            AccountNotVerifiedException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-404")
                .message("El usuario no fue verificado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Usuario no verificado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Auth-Error", "true")
                .body(errorResponse);
    }

//    /**
//     * Manejador de excepciones para solicitudes con acceso no autorizado.
//     * Captura instancias de {@link UnauthorizedAccessException} cuando se intenta acceder a un recurso sin permisos.
//     *
//     * @param ex      la excepción lanzada cuando se intenta acceder a un recurso sin permisos
//     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
//     * @return una respuesta con código 403 (Forbidden) que contiene detalles del error en un objeto {@link ErrorResponse}
//     */
//
//    @ExceptionHandler(UnauthorizedAccessException.class)
//    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(
//            UnauthorizedAccessException ex,
//            WebRequest request) {
//
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .errorCode("AUTH-403")
//                .message("Acceso no autorizado")
//                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
//                .timestamp(Instant.now())
//                .path(getSanitizedPath(request))
//                .build();
//
//        log.warn("Acceso no autorizado - Path: {} | IP: {} | Mensaje: {}",
//                errorResponse.getPath(),
//                request.getHeader("X-Forwarded-For"),
//                ex.getMessage());
//
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .header("X-Content-Type-Options", "nosniff")
//                .header("X-Auth-Error", "true")
//                .body(errorResponse);
//    }

    /**
     * Manejador de excepciones para solicitudes con acceso no autorizado.
     * Captura instancias de {@link AuthorizationDeniedException} cuando se intenta acceder a un recurso sin permisos.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un recurso sin permisos
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 403 (Forbidden) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(
            AuthorizationDeniedException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-403")
                .message("Acceso no autorizado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Acceso no autorizado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Auth-Error", "true")
                .body(errorResponse);
    }


    /**
     * Manejador de excepciones para solicitudes con violaciones de integridad de datos.
     * Captura instancias de {@link DataIntegrityViolationException} cuando se intenta acceder a un recurso sin permisos.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un recurso sin permisos
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 409 (Conflict) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {

        String violationDetail = extractViolationDetail(ex);
        String sanitizedDetail = sanitizeConstraintViolation(violationDetail);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("CONFLICT-001")
                .message("Conflicto con los datos proporcionados")
                .details(List.of(!sanitizedDetail.isEmpty() ?
                        sanitizedDetail : "El recurso ya existe o viola restricciones de unicidad"))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Data Integrity Violation - Path: {} | IP: {} | Violation: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                sanitizedDetail.replaceAll("(\r\n|\n|\r)", ""));

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Conflict-Error", "true")
                .body(errorResponse);
    }


    /**
     * Manejador de excepciones para solicitudes con parámetros faltantes.
     * Captura instancias de {@link MissingPathVariableException} cuando se intenta acceder a un recurso sin parámetros requeridos.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un recurso sin parámetros requeridos
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 400 (Bad Request) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariable(MissingPathVariableException ex, WebRequest request) {

        String paramName = ex.getParameter().getParameterName();
        String userMessage = "Falta un parámetro requerido en la solicitud";
        String detailMessage = String.format("Parámetro faltante: %s", paramName);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("PARAM-001")
                .message(userMessage)
                .details(List.of(detailMessage))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Missing Param - Path: {} | Param: {}",
                errorResponse.getPath(),
                paramName);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    /**
     * Manejador de excepciones para solicitudes con parámetros de tipo incorrecto.
     * Captura instancias de {@link TypeMismatchException} cuando se intenta acceder a un recurso con parámetros de tipo incorrecto.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un recurso con parámetros de tipo incorrecto
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 400 (Bad Request) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(TypeMismatchException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("TYPE-001")
                .message("El parámetro proporcionado no es del tipo correcto")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Type Mismatch - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

//    /**
//     * Manejador de excepciones para solicitudes con recursos no encontrados.
//     * Captura instancias de {@link ProfessionalNotFoundException} cuando se intenta acceder a un profesional que no existe.
//     *
//     * @param ex      la excepción lanzada cuando se intenta acceder a un profesional que no existe
//     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
//     * @return una respuesta con código 404 (Not Found) que contiene detalles del error en un objeto {@link ErrorResponse}
//     */
//
//    @ExceptionHandler(ProfessionalNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleProfessionalNotFoundException(ProfessionalNotFoundException ex, WebRequest request) {
//
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .errorCode("PROFESSIONAL-404")
//                .message("El profesional solicitado no fue encontrado")
//                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
//                .timestamp(Instant.now())
//                .path(getSanitizedPath(request))
//                .build();
//
//        log.warn("Profesional no encontrado - Path: {} | IP: {} | Mensaje: {}",
//                errorResponse.getPath(),
//                request.getHeader("X-Forwarded-For"),
//                ex.getMessage());
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .header("X-Content-Type-Options", "nosniff")
//                .body(errorResponse);
//    }

    /**
     * Manejador de excepciones para solicitudes con recursos no encontrados.
     * Captura instancias de {@link UserNotFoundException} cuando se intenta acceder a un usuario que no existe.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un usuario que no existe
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 404 (Not Found) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("RESOURCE-404")
                .message("El usuario solicitado no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Usuario no se encuentra - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    /**
     * Manejador de excepciones para solicitudes con recursos no encontrados.
     * Captura instancias de {@link DisabledException} cuando se intenta acceder a un usuario deshabilitado.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un usuario deshabilitado
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 403 (Forbidden) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-403")
                .message("El usuario se encuentra deshabilitado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("El usuario se encuentra deshabilitado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    /**
     * Manejador de excepciones para solicitudes con recursos no encontrados.
     * Captura instancias de {@link LockedException} cuando se intenta acceder a un usuario bloqueado.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un usuario bloqueado
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 403 (Forbidden) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(LockedException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-403")
                .message("El usuario se encuentra bloqueado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("El usuario se encuentra bloqueado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    /**
     * Manejador de excepciones para solicitudes con recursos no encontrados.
     * Captura instancias de {@link EmailAlreadyExistsException} cuando se intenta registrar un correo electrónico que ya existe.
     *
     * @param ex      la excepción lanzada cuando se intenta registrar un correo electrónico que ya existe
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 409 (Conflict) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("EMAIL-409")
                .message("El correo electrónico ya existe")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("El correo electrónico ya existe - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }
//
    /**
     * Manejador de excepciones para solicitudes con recursos no encontrados.
     * Captura instancias de {@link PatientNotFoundException} cuando se intenta acceder a un paciente que no existe.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un paciente que no existe
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 404 (Not Found) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePatientNotFoundException(PatientNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("PATIENT-404")
                .message("El paciente solicitado no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Paciente no se encuentra - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    /**
     * Manejador de excepciones para solicitudes con recursos no encontrados.
     * Captura instancias de {@link ProfessionalNotFoundException} cuando se intenta acceder a un profesional que no existe.
     *
     * @param ex      la excepción lanzada cuando se intenta acceder a un profesional que no existe
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 404 (Not Found) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProfessionalNotFoundException(ProfessionalNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("PROFESSIONAL-404")
                .message("El profesional solicitado no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Profesional no se encuentra - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }


    /**
     * Manejador de excepciones para solicitudes con formato inválido en campos de nombre o apellido.
     * Captura instancias de {@link InvalidNameFormatException} lanzadas cuando los campos de búsqueda
     * contienen caracteres no permitidos (como números, símbolos o signos de puntuación).
     * Retorna una respuesta HTTP con código 400 (Bad Request), incluyendo un objeto {@link ErrorResponse}
     * con detalles del error.
     * @param ex      la excepción lanzada debido a un formato inválido en los parámetros de entrada
     * @param request el objeto {@link WebRequest} asociado a la solicitud que provocó la excepción
     * @return una respuesta con código 400 (Bad Request) y detalles del error en el cuerpo
     */
    @ExceptionHandler(InvalidNameFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidNameFormatException(InvalidNameFormatException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("NAME-400")
                .message("Entrada inválida para nombre o apellido")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Formato de nombre inválido - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }


    /**
     * Manejador de excepciones para solicitudes con recursos no encontrados.
     * Captura instancias de {@link Exception} cuando se produce cualquier otra excepción no prevista.
     *
     * @param ex      la excepción lanzada cuando se produce cualquier otra excepción no prevista
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP que provocó la excepción
     * @return una respuesta con código 500 (Internal Server Error) que contiene detalles del error en un objeto {@link ErrorResponse}
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        String rootMessage = Optional.ofNullable(ex.getMessage()).orElse("Error inesperado");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("SERVER-001")
                .message("Ha ocurrido un error inesperado")
                .details(List.of(sanitizeErrorMessage(rootMessage)))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.error("Unhandled Exception - Path: {} | IP: {} | Exception: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }



    // --- Métodos auxiliares ---

    /**
     * Extrae el detalle de violación de restricción de base de datos de una excepción de violación de restricción de base de datos.
     *
     * @param ex la excepción de violación de restricción de base de datos
     * @return el detalle de violación de restricción de base de datos
     */

    private String extractViolationDetail(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        if (rootCause == null) {
            return "Error de integridad de datos no especificado";
        }

        String errorMessage = rootCause.getMessage();
        if (errorMessage == null) {
            return "Violación de restricción de base de datos";
        }

        try {
            if (errorMessage.contains("Duplicate entry") && errorMessage.contains("for key")) {
                int start = errorMessage.indexOf("for key '") + 9;
                int end = errorMessage.indexOf("'", start);
                return end > start ? "El valor ya existe para: " + errorMessage.substring(start, end) : errorMessage;
            }

            if (errorMessage.contains("violates unique constraint")) {
                int start = errorMessage.indexOf("\"") + 1;
                int end = errorMessage.indexOf("\"", start);
                return end > start ? "Restricción única violada: " + errorMessage.substring(start, end) : errorMessage;
            }

            if (errorMessage.contains("unique constraint violated")) {
                int start = errorMessage.indexOf(": ") + 2;
                int end = errorMessage.indexOf(" ", start);
                return end > start ? "Restricción única: " + errorMessage.substring(start, end) : errorMessage;
            }

            return errorMessage.length() > 200 ? errorMessage.substring(0, 200) + "..." : errorMessage;

        } catch (Exception e) {
            log.warn("Error al parsear mensaje de violación de constraint", e);
            return "Error de duplicado (no se pudo determinar el campo)";
        }
    }

    /**
     * Sanitiza el detalle de violación de restricción de base de datos para el cliente.
     *
     * @param detail el detalle de violación de restricción de base de datos
     * @return el detalle de violación de restricción de base de datos sanitizado
     */

    private String sanitizeConstraintViolation(String detail) {
        return detail.replaceAll("(Duplicate entry ')(.*?)(' for key)", "$1[REDACTED]$3")
                .replaceAll("(constraint \\[)(\\w+)(\\])", "$1[REDACTED]$3");
    }



    /**
     * Sanitiza el mensaje de error para el cliente.
     *
     * @param rawMessage el mensaje de error crudo
     * @return el mensaje de error sanitizado
     */

    private String sanitizeErrorMessage(String rawMessage) {
        return rawMessage.replaceAll("(\r\n|\n|\r)", "") // Elimina saltos de línea
                .replaceAll("password|token|secret", "[REDACTED]"); // Ofusca datos sensibles
    }


    /**
     * Sanitiza el nombre del campo para el cliente.
     *
     * @param fieldName el nombre del campo
     * @return el nombre del campo sanitizado
     */
    private String sanitizeFieldName(String fieldName) {
        return fieldName.toLowerCase().contains("password") ? "credential" : fieldName;
    }


    /**
     * Obtiene la ruta sanitizada de la solicitud.
     *
     * @param request el objeto {@link WebRequest} asociado a la solicitud HTTP
     * @return la ruta sanitizada
     */
    private String getSanitizedPath(WebRequest request) {
        return request.getDescription(false)
                .replace("uri=", "")
                .replaceAll("[\\n\\r]", "");
    }

    /**
     * Extrae el mensaje de error de la raíz de una excepción.
     *
     * @param ex la excepción
     * @return el mensaje de error de la raíz
     */

    private String extractRootCauseMessage(Throwable ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        return rootCause != null ?
                rootCause.getMessage() :
                "Error de sintaxis no especificado";
    }

    /**
     * Sanitiza el mensaje de error JSON para el cliente.
     *
     * @param rawMessage el mensaje de error JSON crudo
     * @return el mensaje de error JSON sanitizado
     */

    private String sanitizeJsonErrorMessage(String rawMessage) {
        return rawMessage.replaceAll("(\"|'|`|\\{|\\}|\\[|\\])", "")
                .replaceAll("password|token|secret", "[REDACTED]");
    }

    /**
     * Clasifica el tipo de error JSON.
     *
     * @param message el mensaje de error JSON
     * @return el tipo de error JSON
     */

    private String classifyJsonError(String message) {
        if (message.contains("com.fasterxml.jackson")) {
            return message.contains("UnrecognizedPropertyException") ?
                    "UNKNOWN_FIELD" :
                    "TYPE_MISMATCH";
        }
        return message.contains("JSON parse error") ?
                "SYNTAX_ERROR" :
                "MALFORMED_REQUEST";
    }

    /**
     * Sanitiza el nombre de un parámetro para el cliente.
     *
     * @param name el nombre del parámetro
     * @return el nombre del parámetro sanitizado
     */

    private String sanitizeParamName(String name) {
        return name.toLowerCase().contains("token") || name.toLowerCase().contains("password")
                ? "[REDACTED]"
                : name;
    }

    /**
     * Sanitiza la ruta de un parámetro para el cliente.
     *
     * @param path la ruta del parámetro
     * @return la ruta del parámetro sanitizada
     */
    private String sanitizePropertyPath(String path) {
        return path.toLowerCase().contains("password") ? "credential" : path.replaceAll("[\\n\\r]", "");
    }

        // -- Inserciones para manejar excepciones del módulo appointments --
    /**
     * Manejador de excepciones para citas médicas no encontradas.
     * Captura instancias de {@link AppointmentNotFoundException} cuando no se encuentra una cita en la base de datos.
     *
     * @param ex      la excepción lanzada cuando la cita no existe
     * @param request el objeto {@link WebRequest} asociado a la solicitud
     * @return una respuesta con código 404 (Not Found) que contiene detalles del error
     */
    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentNotFound(
            AppointmentNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("APPT-404")
                .message("La cita médica solicitada no fue encontrada")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Cita médica no encontrada - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    /**
     * Manejador de excepciones para conflictos al agendar o modificar citas.
     * Captura instancias de {@link AppointmentConflictException} cuando hay solapamiento de horarios o conflicto de estado.
     *
     * @param ex      la excepción lanzada cuando existe conflicto de horario o estado
     * @param request el objeto {@link WebRequest} asociado a la solicitud
     * @return una respuesta con código 409 (Conflict) que contiene detalles del error
     */
    @ExceptionHandler(AppointmentConflictException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentConflict(
            AppointmentConflictException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("APPT-409")
                .message("Conflicto al programar o modificar la cita médica")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Conflicto de cita médica - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }
}
