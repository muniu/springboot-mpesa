package co.ke.integration.mpesa.exception;

/**
 * Exception thrown when M-Pesa API times out.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
public class MpesaTimeoutException extends MpesaApiException {

    public MpesaTimeoutException(String message) {
        super(message);
    }

    public MpesaTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
