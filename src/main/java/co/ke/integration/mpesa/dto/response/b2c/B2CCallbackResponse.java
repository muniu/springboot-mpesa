package co.ke.integration.mpesa.dto.response.b2c;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class B2CCallbackResponse {

    @JsonProperty("Result")
    private Result result;

    @Data
    public static class Result {
        @JsonProperty("ResultType")
        private int resultType;

        @JsonProperty("ResultCode")
        private String resultCode;

        @JsonProperty("ResultDesc")
        private String resultDesc;

        @JsonProperty("OriginatorConversationID")
        private String originatorConversationID;

        @JsonProperty("ConversationID")
        private String conversationID;

        @JsonProperty("TransactionID")
        private String transactionID;

        @JsonProperty("ResultParameters")
        private ResultParameters resultParameters;
    }

    @Data
    public static class ResultParameters {
        @JsonProperty("ResultParameter")
        private ResultParameter[] resultParameter;
    }

    @Data
    public static class ResultParameter {
        @JsonProperty("Key")
        private String key;

        @JsonProperty("Value")
        private String value;
    }
}
