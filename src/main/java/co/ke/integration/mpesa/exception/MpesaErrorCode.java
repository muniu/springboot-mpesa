package co.ke.integration.mpesa.exception;

/**
 * Enumeration of M-Pesa API error codes and their descriptions.
 * Based on official M-Pesa API documentation.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
public enum MpesaErrorCode {
    // Authentication Errors
    INVALID_ACCESS_TOKEN("400.003.01", "Invalid Access Token", "Incorrect or expired access token", ErrorCategory.AUTH),
    INVALID_AUTH_HEADER("404.001.04", "Invalid Authentication Header", "Incorrect authorization type", ErrorCategory.AUTH),

    // General Errors
    INTERNAL_SERVER_ERROR("500.003.1001", "Internal Server Error", "Server failure", ErrorCategory.SERVER),
    BAD_REQUEST("400.003.02", "Bad Request", "The server cannot process the request because something is missing", ErrorCategory.CLIENT),
    RESOURCE_NOT_FOUND("404.003.01", "Resource not found", "The requested resource could not be found", ErrorCategory.CLIENT),

    // Rate Limiting Errors
    QUOTA_VIOLATION("500.003.03", "Error Occurred: Quota Violation", "Multiple requests violating transaction per second speed", ErrorCategory.RATE_LIMIT),
    SPIKE_ARREST("500.003.02", "Error Occurred: Spike Arrest Violation", "Endpoints generating too many errors", ErrorCategory.RATE_LIMIT),

    // B2B Specific Errors
    INVALID_INITIATOR("2001", "The initiator information is invalid.", "Invalid initiator credentials", ErrorCategory.B2B),
    DUPLICATE_DETECTED("15", "Duplicate Detected", "Duplicate originator conversation id", ErrorCategory.B2B),

    // Balance Specific Errors
    INVALID_PARTY_INFO("400.002.02", "Invalid party information", "Invalid party details provided", ErrorCategory.BALANCE),

    // Invalid Request Payload
    INVALID_PAYLOAD("400.002.05", "Invalid Request Payload", "Request body is not properly formatted", ErrorCategory.CLIENT);

    private final String code;
    private final String message;
    private final String description;
    private final ErrorCategory category;

    MpesaErrorCode(String code, String message, String description, ErrorCategory category) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.category = category;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getDescription() { return description; }
    public ErrorCategory getCategory() { return category; }

    public static MpesaErrorCode fromCode(String code) {
        for (MpesaErrorCode error : values()) {
            if (error.code.equals(code)) {
                return error;
            }
        }
        return null;
    }
}

