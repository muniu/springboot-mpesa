package co.ke.integration.mpesa.dto.request.c2b;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for C2B payment validation/confirmation request from M-Pesa.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class C2BPaymentRequest {

    @JsonProperty("TransactionType")
    private String transactionType;

    @JsonProperty("TransID")
    private String transactionId;

    @JsonProperty("TransTime")
    private String transactionTime;

    @JsonProperty("TransAmount")
    private String amount;

    @JsonProperty("BusinessShortCode")
    private String businessShortCode;

    @JsonProperty("BillRefNumber")
    private String billRefNumber;

    @JsonProperty("InvoiceNumber")
    private String invoiceNumber;

    @JsonProperty("OrgAccountBalance")
    private String orgAccountBalance;

    @JsonProperty("ThirdPartyTransID")
    private String thirdPartyTransId;

    @JsonProperty("MSISDN")
    private String msisdn;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("MiddleName")
    private String middleName;

    @JsonProperty("LastName")
    private String lastName;
}

