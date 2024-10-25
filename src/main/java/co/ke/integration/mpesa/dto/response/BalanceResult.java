package co.ke.integration.mpesa.dto.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for M-Pesa Account Balance Result Callback.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceResult {
    private String resultType;
    private String resultCode;
    private String resultDesc;
    private String originatorConversationID;
    private String conversationID;
    private String transactionID;
    private ResultParameters resultParameters;
    private ReferenceData referenceData;

    @Data
    public static class ResultParameters {
        private List<ResultParameter> resultParameter;
    }

    @Data
    public static class ResultParameter {
        private String key;
        private String value;
    }

    @Data
    public static class ReferenceData {
        private ReferenceItem referenceItem;
    }

    @Data
    public static class ReferenceItem {
        private String key;
        private String value;
    }
}