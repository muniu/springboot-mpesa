package co.ke.integration.mpesa.dto.request.c2b;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for registering C2B URLs with M-Pesa.
 * These URLs will receive validation and confirmation callbacks.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class C2BRegisterUrlRequest {

    @JsonProperty("ShortCode")
    private String shortCode;

    @JsonProperty("ResponseType")
    private String responseType;  // "Completed" or "Cancelled"

    @JsonProperty("ConfirmationURL")
    private String confirmationURL;

    @JsonProperty("ValidationURL")
    private String validationURL;
}

