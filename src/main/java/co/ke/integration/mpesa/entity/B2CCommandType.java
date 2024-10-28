package co.ke.integration.mpesa.entity;

public enum B2CCommandType {
    SALARY_PAYMENT("SalaryPayment"),
    BUSINESS_PAYMENT("BusinessPayment"),
    PROMOTION_PAYMENT("PromotionPayment");

    private final String value;

    B2CCommandType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static B2CCommandType fromString(String text) {
        for (B2CCommandType type : B2CCommandType.values()) {
            if (type.value.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown command type: " + text);
    }
}
