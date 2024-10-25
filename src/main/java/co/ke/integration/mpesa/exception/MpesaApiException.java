package co.ke.integration.mpesa.exception;

/**
 * Base exception class for M-Pesa API related errors.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */

public class MpesaApiException extends RuntimeException {
    private final MpesaErrorCode errorCode;
    private final String requestId;
    private final ErrorCategory category;
    public MpesaApiException(MpesaErrorCode errorCode, String requestId) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.requestId = requestId;
        this.category = errorCode.getCategory();
    }

    public MpesaApiException(MpesaErrorCode errorCode, String requestId, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.requestId = requestId;
        this.category = errorCode.getCategory();
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }

    public String getRequestId() {
        return requestId;
    }
    public ErrorCategory getCategory() {
        return category;
    }
    public String getDescription() {
        return errorCode.getDescription();
    }
}