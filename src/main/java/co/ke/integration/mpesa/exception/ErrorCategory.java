package co.ke.integration.mpesa.exception;

/**
 * Categories of M-Pesa errors for better organization and handling.
 */
public enum ErrorCategory {
    AUTH,
    CLIENT,
    SERVER,
    RATE_LIMIT,
    B2B,
    B2C,
    C2B,
    BALANCE
}
