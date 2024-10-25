package co.ke.integration.mpesa.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler for M-Pesa API related errors.
 * Provides consistent error responses across the application.
 * Maps specific M-Pesa error codes to appropriate HTTP responses.
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MpesaApiException.class)
    public ResponseEntity<ErrorResponse> handleMpesaApiException(MpesaApiException ex) {
        log.error("M-Pesa API error: {} - {}", ex.getErrorCode(), ex.getMessage(), ex);

        HttpStatus status = determineHttpStatus(ex.getCategory());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(ex.getCategory().name())
                .message(ex.getMessage())
                .description(ex.getDescription())
                .errorCode(ex.getErrorCode())
                .requestId(ex.getRequestId())
                .build();

        return new ResponseEntity<>(error, status);
    }

    private HttpStatus determineHttpStatus(ErrorCategory category) {
        return switch (category) {
            case AUTH -> HttpStatus.UNAUTHORIZED;
            case CLIENT -> HttpStatus.BAD_REQUEST;
            case SERVER -> HttpStatus.INTERNAL_SERVER_ERROR;
            case RATE_LIMIT -> HttpStatus.TOO_MANY_REQUESTS;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}