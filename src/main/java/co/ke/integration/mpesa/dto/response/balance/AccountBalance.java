package co.ke.integration.mpesa.dto.response.balance;

import lombok.Data;

/**
 * Represents parsed account balances from M-Pesa response.
 * Format: "Working Account|KES|346568.83|6186.83|340382.00|0.00"
 */
@Data
public class AccountBalance {
    private String accountType;
    private String currency;
    private double currentBalance;
    private double availableBalance;
    private double reservedBalance;
    private double unclearedBalance;

    public static AccountBalance fromString(String balanceString) {
        String[] parts = balanceString.split("\\|");
        if (parts.length >= 6) {
            AccountBalance balance = new AccountBalance();
            balance.setAccountType(parts[0]);
            balance.setCurrency(parts[1]);
            balance.setCurrentBalance(parseDouble(parts[2]));
            balance.setAvailableBalance(parseDouble(parts[3]));
            balance.setReservedBalance(parseDouble(parts[4]));
            balance.setUnclearedBalance(parseDouble(parts[5]));
            return balance;
        }
        return null;
    }

    private static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%.2f|%.2f|%.2f|%.2f",
                accountType, currency, currentBalance, availableBalance,
                reservedBalance, unclearedBalance);
    }
}