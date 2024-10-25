package co.ke.integration.mpesa.dto.response.balance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response received in the callback for Account Balance query.
 * <p>
 * Example response:
 * {
 * "Result": {
 * "ResultType": 0,
 * "ResultCode": 0,
 * "ResultDesc": "The service request is processed successfully",
 * "OriginatorConversationID": "16917-22577599-3",
 * "ConversationID": "AG_20200206_00005e091a8ec6b9eac5",
 * "TransactionID": "OA90000000",
 * "ResultParameters": {
 * "ResultParameter": [
 * {
 * "Key": "AccountBalance",
 * "Value": "Working Account|KES|346568.83|6186.83|340382.00|0.00"
 * },
 * {
 * "Key": "BOCompletedTime",
 * "Value": "20200109125710"
 * }
 * ]
 * },
 * "ReferenceData": {
 * "ReferenceItem": {
 * "Key": "QueueTimeoutURL",
 * "Value": "https://internalsandbox.safaricom.co.ke/mpesa/abresults/v1/submit"
 * }
 * }
 * }
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceCallbackResponse {

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

        @JsonProperty("ReferenceData")
        private ReferenceData referenceData;
    }

    @Data
    public static class ResultParameters {
        @JsonProperty("ResultParameter")
        private List<ResultParameter> resultParameter;
    }

    @Data
    public static class ResultParameter {
        @JsonProperty("Key")
        private String key;

        @JsonProperty("Value")
        private String value;
    }

    @Data
    public static class ReferenceData {
        @JsonProperty("ReferenceItem")
        private ReferenceItem referenceItem;
    }

    @Data
    public static class ReferenceItem {
        @JsonProperty("Key")
        private String key;

        @JsonProperty("Value")
        private String value;
    }

    /**
     * Helper method to parse account balances from the response.
     *
     * @return AccountBalance object containing parsed balances
     */
    public AccountBalance getAccountBalance() {
        if (result != null &&
                result.getResultParameters() != null &&
                result.getResultParameters().getResultParameter() != null) {

            for (ResultParameter param : result.getResultParameters().getResultParameter()) {
                if ("AccountBalance".equals(param.getKey())) {
                    return AccountBalance.fromString(param.getValue());
                }
            }
        }
        return null;
    }
}
