package co.ke.integration.mpesa.dto.response.c2b;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for confirmation response back to M-Pesa.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class C2BConfirmationResponse {

    @JsonProperty("ResultCode")
    private String resultCode;  // Always "0"

    @JsonProperty("ResultDesc")
    private String resultDesc;  // Usually "Success"

    public static C2BConfirmationResponse success() {
        return new C2BConfirmationResponse("0", "Success");
    }
}