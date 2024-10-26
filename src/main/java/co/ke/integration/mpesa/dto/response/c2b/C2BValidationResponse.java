package co.ke.integration.mpesa.dto.response.c2b;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for validation response back to M-Pesa.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class C2BValidationResponse {

    @JsonProperty("ResultCode")
    private String resultCode;  // "0" for success, others for rejection

    @JsonProperty("ResultDesc")
    private String resultDesc;  // "Accepted" or "Rejected"

    public static C2BValidationResponse accepted() {
        return new C2BValidationResponse("0", "Accepted");
    }

    public static C2BValidationResponse rejected(String code, String reason) {
        return new C2BValidationResponse(code, reason);
    }
}
