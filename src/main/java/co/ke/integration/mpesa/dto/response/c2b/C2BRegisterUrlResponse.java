package co.ke.integration.mpesa.dto.response.c2b;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response from M-Pesa after URL registration attempt.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class C2BRegisterUrlResponse {

    @JsonProperty("OriginatorCoversationID")
    private String originatorConversationID;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;
}
