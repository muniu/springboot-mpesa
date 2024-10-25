package co.ke.integration.mpesa.exception;

/**
 * Exception thrown when authentication with M-Pesa API fails.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
public class MpesaAuthenticationException extends MpesaApiException {

    public MpesaAuthenticationException(String message) {
        super(message);
    }

    public MpesaAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}