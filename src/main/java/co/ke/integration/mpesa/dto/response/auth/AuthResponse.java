package co.ke.integration.mpesa.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Response from M-PESA OAuth endpoint.
 * Example response:
 * {
 *    "access_token": "c9SQxWWhmdVRlyh0zh8gZDTkubVF",
 *    "expires_in": "3599"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

    /**
     * Access token to access other APIs
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * Token expiry time in seconds (typically 3599)
     */
    @JsonProperty("expires_in")
    private String expiresIn;

    /**
     * Convert expires_in from String to long
     * @return expiry time in seconds
     */
    public long getExpiresInSeconds() {
        try {
            return Long.parseLong(expiresIn);
        } catch (NumberFormatException e) {
            // Default to 3599 seconds (1 hour - 1 second) if parsing fails
            return 3599L;
        }
    }
}
