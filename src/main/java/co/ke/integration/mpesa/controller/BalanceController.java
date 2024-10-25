package co.ke.integration.mpesa.controller;

import co.ke.integration.mpesa.dto.request.BalanceRequest;
import co.ke.integration.mpesa.dto.response.BalanceResponse;
import co.ke.integration.mpesa.dto.response.BalanceResult;
import co.ke.integration.mpesa.service.BalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling M-Pesa Account Balance operations.
 * Provides endpoints for querying account balances and receiving callbacks.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
@RestController
@RequestMapping("/api/mpesa/balance")
@Slf4j
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    /**
     * Query account balance from M-Pesa.
     *
     * @param request Balance query request
     * @return ResponseEntity containing the balance response
     */
    @PostMapping("/query")
    public ResponseEntity<BalanceResponse> queryBalance(@RequestBody @Valid BalanceRequest request) {
        log.info("Received balance query request for shortcode: {}", request.getPartyA());
        BalanceResponse response = balanceService.queryBalance(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Handle balance query result callback from M-Pesa.
     *
     * @param result Balance query result
     * @return ResponseEntity acknowledging the callback
     */
    @PostMapping("/result")
    public ResponseEntity<String> handleBalanceResult(@RequestBody BalanceResult result) {
        log.info("Received balance result callback for conversation ID: {}",
                result.getConversationID());
        balanceService.processBalanceResult(result);
        return ResponseEntity.ok("Success");
    }
}