package co.ke.integration.mpesa.service;

import co.ke.integration.mpesa.client.MpesaApiClient;
import co.ke.integration.mpesa.config.MpesaConfig;
import co.ke.integration.mpesa.dto.request.balance.BalanceRequest;
import co.ke.integration.mpesa.dto.response.balance.AccountBalance;
import co.ke.integration.mpesa.dto.response.balance.BalanceCallbackResponse;
import co.ke.integration.mpesa.dto.response.balance.BalanceResponse;
import co.ke.integration.mpesa.entity.BalanceQuery;
import co.ke.integration.mpesa.entity.TransactionStatus;
import co.ke.integration.mpesa.repository.BalanceQueryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private final MpesaApiClient mpesaApiClient;
    private final AuthService authService;
    private final MpesaConfig mpesaConfig;
    private final BalanceQueryRepository balanceQueryRepository;
    private final ObjectMapper objectMapper;

    /**
     * Query account balance from M-Pesa.
     * @param request Balance request details
     * @return BalanceResponse containing the request tracking information
     */
    public BalanceResponse queryBalance(BalanceRequest request) {
        log.info("Initiating balance query for party: {}", request.getPartyA());

        BalanceQuery balanceQuery = BalanceQuery.builder()
                .partyA(request.getPartyA())
                .status(TransactionStatus.INITIATED)
                .rawRequest(toJson(request))
                .build();

        balanceQueryRepository.save(balanceQuery);

        String accessToken = authService.getAccessToken();
        BalanceResponse response = mpesaApiClient.call(
                mpesaConfig.getBalanceurl(),
                request,
                BalanceResponse.class,
                accessToken
        );

        balanceQuery.setConversationId(response.getConversationID());
        balanceQuery.setOriginatorConversationId(response.getOriginatorConversationID());
        balanceQuery.setStatus(TransactionStatus.PENDING);
        balanceQuery.setRawResponse(toJson(response));
        balanceQueryRepository.save(balanceQuery);

        return response;

    }

    /**
     * Processes timeout notification for balance query.
     *
     * @param timeoutResponse the timeout notification from M-Pesa
     */
    public void processBalanceTimeout(BalanceCallbackResponse timeoutResponse) {
        String conversationId = timeoutResponse.getResult().getConversationID();
        log.warn("Processing balance timeout for conversation ID: {}", conversationId);

        try {
            // Log the timeout details
            log.info("Balance query timeout - Result Code: {}, Description: {}",
                    timeoutResponse.getResult().getResultCode(),
                    timeoutResponse.getResult().getResultDesc()
            );

            // We might want to:
            // 1. Store the timeout information
            // 2. Trigger a retry mechanism
            // 3. Notify relevant parties
            // 4. Update transaction status

            handleBalanceQueryTimeout(timeoutResponse);

        } catch (Exception e) {
            log.error("Error processing balance timeout for conversation ID: {}",
                    conversationId, e);
        }
    }

    /**
     * Handle timeout scenario for balance query.
     *
     * @param timeoutResponse the timeout response from M-Pesa
     */
    private void handleBalanceQueryTimeout(BalanceCallbackResponse timeoutResponse) {
        String conversationId = timeoutResponse.getResult().getConversationID();

        // You could implement retry logic here
        log.warn("Balance query timed out for conversation ID: {} - may need manual reconciliation",
                conversationId);

        // We might want to store the timeout information for reconciliation
        // balanceRepository.updateTransactionStatus(conversationId, TransactionStatus.TIMEOUT);

        // Or notify monitoring systems
        // alertService.notifyBalanceQueryTimeout(conversationId);
    }

    /**
     * Processes balance query callback from M-Pesa.
     *
     * @param callback the callback response from M-Pesa
     */
    public void processBalanceCallback(BalanceCallbackResponse callback) {
        String conversationId = callback.getResult().getConversationID();
        log.info("Processing balance callback for conversation ID: {}", conversationId);

        BalanceQuery balanceQuery = balanceQueryRepository.findByConversationId(conversationId)
                .orElse(null);

        if (balanceQuery == null) {
            log.warn("No balance query found for conversation ID: {}", conversationId);
            return;
        }

        try {
            if ("0".equals(callback.getResult().getResultCode())) {
                AccountBalance balance = callback.getAccountBalance();
                if (balance != null) {
                    balanceQuery.setStatus(TransactionStatus.COMPLETED);
                }
            } else {
                balanceQuery.setStatus(TransactionStatus.FAILED);
                balanceQuery.setResultCode(callback.getResult().getResultCode());
                balanceQuery.setResultDesc(callback.getResult().getResultDesc());
            }
            balanceQueryRepository.save(balanceQuery);

        } catch (Exception e) {
            log.error("Error processing balance callback for conversation ID: {}",
                    conversationId, e);
            balanceQuery.setStatus(TransactionStatus.FAILED);
            balanceQuery.setResultDesc(e.getMessage());
            balanceQueryRepository.save(balanceQuery);
        }
    }



    /**
     * Process different types of account balances.
     *
     * @param balance the account balance information
     */
    private void processAccountBalance(AccountBalance balance) {
        switch (balance.getAccountType()) {
            case "Working Account" -> processWorkingAccountBalance(balance);
            case "Utility Account" -> processUtilityAccountBalance(balance);
            case "Charges Paid Account" -> processChargesPaidAccountBalance(balance);
            case "Organization Settlement Account" -> processSettlementAccountBalance(balance);
            default -> log.warn("Unknown account type: {}", balance.getAccountType());
        }
    }

    private void processWorkingAccountBalance(AccountBalance balance) {
        log.info("Working Account Balance: {} {}",
                balance.getCurrentBalance(), balance.getCurrency());
        // Add business logic for working account balance
    }

    private void processUtilityAccountBalance(AccountBalance balance) {
        log.info("Utility Account Balance: {} {}",
                balance.getCurrentBalance(), balance.getCurrency());
        // Add business logic for utility account balance
    }

    private void processChargesPaidAccountBalance(AccountBalance balance) {
        log.info("Charges Paid Account Balance: {} {}",
                balance.getCurrentBalance(), balance.getCurrency());
        // Add business logic for charges paid account balance
    }

    private void processSettlementAccountBalance(AccountBalance balance) {
        log.info("Settlement Account Balance: {} {}",
                balance.getCurrentBalance(), balance.getCurrency());
        // Add business logic for settlement account balance
    }

    /**
     * Convert object to JSON string.
     *
     * @param object Object to convert
     * @return JSON string or null if conversion fails
     */
    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Error converting object to JSON", e);
            return null;
        }
    }

}