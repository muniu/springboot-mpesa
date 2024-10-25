package co.ke.integration.mpesa.dto.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO for M-Pesa Account Balance Response.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceResponse {
    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;

    @JsonProperty("ConversationID")
    private String conversationID;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;
}
