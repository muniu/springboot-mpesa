package co.ke.integration.mpesa.dto.request.b2c;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class B2CRequest {

    @JsonProperty("InitiatorName")
    private String initiatorName;

    @JsonProperty("SecurityCredential")
    private String securityCredential;

    @JsonProperty("CommandID")
    private String commandID;  // SalaryPayment, BusinessPayment, PromotionPayment

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("PartyA")
    private String partyA;  // Shortcode

    @JsonProperty("PartyB")
    private String partyB;  // Phone number

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("QueueTimeOutURL")
    private String queueTimeOutURL;

    @JsonProperty("ResultURL")
    private String resultURL;

    @JsonProperty("Occassion")
    private String occasion;
}

