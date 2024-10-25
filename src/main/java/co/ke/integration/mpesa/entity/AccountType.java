package co.ke.integration.mpesa.entity;

public enum AccountType {
    WORKING_ACCOUNT("Working Account"),
    UTILITY_ACCOUNT("Utility Account"),
    CHARGES_PAID_ACCOUNT("Charges Paid Account"),
    SETTLEMENT_ACCOUNT("Organization Settlement Account");

    private final String mpesaName;

    AccountType(String mpesaName) {
        this.mpesaName = mpesaName;
    }

    public static AccountType fromMpesaName(String mpesaName) {
        for (AccountType type : values()) {
            if (type.mpesaName.equals(mpesaName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown account type: " + mpesaName);
    }
}
