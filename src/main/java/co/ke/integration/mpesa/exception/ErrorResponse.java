package co.ke.integration.mpesa.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Standardized error response for the API.
 */
@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String description;
    private String errorCode;
    private String requestId;
}
