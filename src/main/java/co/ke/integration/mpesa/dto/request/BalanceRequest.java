package co.ke.integration.mpesa.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for M-Pesa Account Balance Request.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceRequest {
    private String initiator;
    private String securityCredential;
    private String commandID;  // Always "AccountBalance"
    private String partyA;     // The shortcode of the organization
    private String identifierType;  // Type of organization
    private String remarks;
    private String queueTimeOutURL;
    private String resultURL;
}