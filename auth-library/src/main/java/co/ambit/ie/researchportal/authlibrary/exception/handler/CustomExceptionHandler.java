package co.ambit.ie.researchportal.authlibrary.exception.handler;

import co.ambit.ie.researchportal.authlibrary.exception.dto.ErrorResponseDTO;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {
    private final Tracer tracer;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(final MethodArgumentNotValidException exception,
                                                                   final HttpServletRequest request) {
        Map<String, String> errors = exception.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, error ->
                        Optional.ofNullable(error.getDefaultMessage())
                                .orElse("")));

        return ResponseEntity.badRequest()
                .body(this.buidlErrorResponseDTO(
                        HttpStatus.BAD_REQUEST,
                        "VALIDATION_ERROR",
                        "Invalid request data",
                        request.getRequestURI(),
                        errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(this.buidlErrorResponseDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "INTERNAL_SERVER_ERROR",
                        "Something went wrong. Please try again later.",
                        request.getRequestURI(),
                        null));
    }

    private ErrorResponseDTO buidlErrorResponseDTO(final HttpStatus status,
                                                   final String error,
                                                   final String message,
                                                   final String path,
                                                   final Object errors) {
        return ErrorResponseDTO.builder()
                .traceId(Optional.ofNullable(this.tracer
                                .currentSpan())
                        .map(span -> span.context().traceId())
                        .orElse(null))
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .data(errors)
                .build();
    }
}
