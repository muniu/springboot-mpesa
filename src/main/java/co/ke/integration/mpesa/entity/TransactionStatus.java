package co.ke.integration.mpesa.entity;

/**
 * Enum representing M-Pesa transaction states.
 */
public enum TransactionStatus {
    INITIATED,     // Request created in our system
    PENDING,       // Awaiting callback from M-Pesa
    COMPLETED,     // Successfully completed
    FAILED,        // Failed transaction
    TIMEOUT        // No response received in time
}
