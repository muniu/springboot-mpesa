package co.ke.integration.mpesa.exception;

/**
 * Complete enumeration of M-Pesa API error codes.
 * Sourced directly from M-Pesa API documentation.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
public enum MpesaErrorCode {
    // Server/API Infrastructure Errors
    INTERNAL_SERVER_ERROR("500.003.1001", "Internal Server Error", "Server failure"),
    QUOTA_VIOLATION("500.003.03", "Error Occurred: Quota Violation", "Multiple requests violating M-PESA transaction per second speed"),
    SPIKE_ARREST("500.003.02", "Error Occurred: Spike Arrest Violation", "Endpoints constantly generate lot of errors that lead to spike that affects M-PESA performance"),

    // Authentication & Authorization Errors
    INVALID_ACCESS_TOKEN("400.003.01", "Invalid Access Token", "Incorrect or expired access token"),
    INVALID_AUTH_HEADER("404.001.04", "Invalid Authentication Header", "All M-PESA APIs on Daraja platform are POST except Authorization API which is GET"),

    // Request Format Errors
    BAD_REQUEST("400.003.02", "Bad Request", "The server cannot process the request because something is missing"),
    INVALID_REQUEST_PAYLOAD("400.002.05", "Invalid Request Payload", "Request body is not properly drafted"),
    RESOURCE_NOT_FOUND("404.003.01", "Resource not found", "The requested resource could not be found"),

    // B2B and Transaction Errors
    INITIATOR_INVALID("2001", "The initiator information is invalid", "Invalid initiator credentials"),
    DUPLICATE_DETECTED("15", "Duplicate Detected", "Duplicate originator conversation id"),
    INTERNAL_FAILURE("17", "Internal Failure", "Failures not identified more specifically"),
    INITIATOR_CREDENTIAL_CHECK_FAILURE("18", "Initiator Credential Check Failure", "Password check failed for initiator"),
    MESSAGE_SEQUENCING_FAILURE("19", "Message Sequencing Failure", "Message sequencing has failed"),
    UNRESOLVED_INITIATOR("20", "Unresolved Initiator", "The initiator username cannot be found"),
    INITIATOR_PERMISSION_FAILURE("21", "Initiator to Primary Party Permission Failure", "Initiator not authorized for primary party"),
    INITIATOR_RECEIVER_PERMISSION_FAILURE("22", "Initiator to Receiver Party Permission Failure", "Initiator not authorized for receiver party"),
    MISSING_MANDATORY_FIELDS("24", "Missing mandatory fields", "Required parameters are missing"),
    INVALID_REQUEST_PARAMETERS("25", "InvalidRequestParameters", "Invalid parameter format or value"),
    TRAFFIC_BLOCKING("26", "Traffic blocking condition in place", "System is too busy"),
    INVALID_COMMAND("29", "InvalidCommand", "Command specified is not defined"),

    // Transaction Processing Errors
    REQUEST_CACHED("100000000", "Request was cached, waiting for resending", "Request is in cache"),
    SYSTEM_OVERLOAD("100000001", "The system is overload", "System overload condition"),
    THROTTLING_ERROR("100000002", "Throttling error", "Request throttled"),
    SERVER_ERROR("100000004", "Internal Server Error", "Internal server error occurred"),
    INVALID_INPUT("100000005", "Invalid input value", "Invalid parameter value"),
    SERVICE_STATUS_ERROR("100000007", "Service's status is abnormal", "Service status abnormal"),
    API_STATUS_ERROR("100000009", "API's status is abnormal", "API status abnormal"),
    INSUFFICIENT_PERMISSIONS("100000010", "Insufficient permissions", "Insufficient permissions for operation"),
    REQUEST_RATE_EXCEEDED("100000011", "Exceed the limitation of request rate", "Request rate limit exceeded"),

    // System Maintenance
    SYSTEM_MAINTENANCE("00.002.1001", "Service is currently under maintenance", "System maintenance in progress");

    private final String code;
    private final String message;
    private final String description;

    MpesaErrorCode(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Find MpesaErrorCode by error code string.
     *
     * @param code the error code to look up
     * @return matching MpesaErrorCode or INTERNAL_SERVER_ERROR if not found
     */
    public static MpesaErrorCode fromCode(String code) {
        if (code == null) {
            return INTERNAL_SERVER_ERROR;
        }

        for (MpesaErrorCode error : values()) {
            if (error.code.equals(code)) {
                return error;
            }
        }

        return INTERNAL_SERVER_ERROR;
    }

    /**
     * Check if the error is retriable.
     * Some errors (like system overload) might be resolved by retrying.
     *
     * @return true if the error might be resolved by retrying
     */
    public boolean isRetriable() {
        return switch (this) {
            case SYSTEM_OVERLOAD, TRAFFIC_BLOCKING, REQUEST_CACHED,
                    THROTTLING_ERROR, SERVER_ERROR, SYSTEM_MAINTENANCE -> true;
            default -> false;
        };
    }

    /**
     * Check if the error is related to authentication.
     *
     * @return true if the error is authentication-related
     */
    public boolean isAuthError() {
        return switch (this) {
            case INVALID_ACCESS_TOKEN, INVALID_AUTH_HEADER,
                    INITIATOR_CREDENTIAL_CHECK_FAILURE, UNRESOLVED_INITIATOR,
                    INITIATOR_PERMISSION_FAILURE, INSUFFICIENT_PERMISSIONS -> true;
            default -> false;
        };
    }
}