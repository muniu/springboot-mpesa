package co.ke.integration.mpesa.service;

import co.ke.integration.mpesa.config.MpesaConfig;
import co.ke.integration.mpesa.config.MpesaMetrics;
import co.ke.integration.mpesa.dto.request.BalanceRequest;
import co.ke.integration.mpesa.dto.response.BalanceResponse;
import co.ke.integration.mpesa.dto.response.BalanceResult;
import co.ke.integration.mpesa.exception.MpesaApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for handling M-Pesa Account Balance queries.
 * Supports balance checking for different account types:
 * - Working/MMF Account
 * - Utility Account
 * - Charges Paid Account
 * - Organization Settlement Account
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceService {

    private final RestTemplate restTemplate;
    private final MpesaConfig mpesaConfig;
    private final AuthService authService;
    private final MpesaMetrics metrics;

    @Value("${mpesa.balance.url}")
    private String balanceUrl;

    /**
     * Query account balance from M-Pesa.
     *
     * @param request Balance request details
     * @return BalanceResponse containing the request tracking information
     *
     */
    public BalanceResponse queryBalance(BalanceRequest request) {

        try {
            log.info("Initiating balance query for shortcode: {}", request.getPartyA());


            String accessToken = authService.getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<BalanceRequest> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<BalanceResponse> response = restTemplate.exchange(
                    balanceUrl,
                    HttpMethod.POST,
                    requestEntity,
                    BalanceResponse.class
            );

            if (response.getBody() == null) {
                //throw new MpesaApiException("Null response received from M-Pesa API");
            }

            log.info("Balance query initiated successfully. Conversation ID: {}",
                    response.getBody().getConversationID());
            return response.getBody();

        } catch (Exception e) {

            log.error("Error querying balance: {}", e.getMessage(), e);
            throw new MpesaApiException("Failed to query balance", e);
        } finally {

        }
    }

    /**
     * Process balance query result callback from M-Pesa.
     *
     * @param result Balance query result from callback
     */
    public void processBalanceResult(BalanceResult result) {
        log.info("Processing balance result for conversation ID: {}", result.getConversationID());

        try {
            if ("0".equals(result.getResultCode())) {
                Map<String, String> balances = extractBalances(result);
                // Store or process the balances as needed
                log.info("Balance query successful. Working Account Balance: {}",
                        balances.get("WorkingAccountBalance"));
            } else {
                log.error("Balance query failed. Result code: {}, Description: {}",
                        result.getResultCode(), result.getResultDesc());

            }
        } catch (Exception e) {
            log.error("Error processing balance result: {}", e.getMessage(), e);

        }
    }

    /**
     * Extract account balances from the result parameters.
     *
     * @param result Balance query result
     * @return Map of account types to their balances
     */
    private Map<String, String> extractBalances(BalanceResult result) {
        Map<String, String> balances = new HashMap<>();
        if (result.getResultParameters() != null &&
                result.getResultParameters().getResultParameter() != null) {

            for (BalanceResult.ResultParameter param :
                    result.getResultParameters().getResultParameter()) {
                if ("AccountBalance".equals(param.getKey())) {
                    parseAccountBalances(param.getValue(), balances);
                }
            }
        }
        return balances;
    }

    /**
     * Parse the account balance string into individual account balances.
     *
     * @param balanceString Raw balance string from M-Pesa
     * @param balances Map to store parsed balances
     */
    private void parseAccountBalances(String balanceString, Map<String, String> balances) {
        String[] accountBalances = balanceString.split("&");
        for (String balance : accountBalances) {
            String[] parts = balance.split("\\|");
            if (parts.length >= 2) {
                balances.put(parts[0].trim() + "Balance", parts[1].trim());
            }
        }
    }
}