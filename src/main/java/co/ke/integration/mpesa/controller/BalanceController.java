package co.ke.integration.mpesa.controller;

import co.ke.integration.mpesa.dto.request.balance.BalanceRequest;
import co.ke.integration.mpesa.dto.response.balance.BalanceCallbackResponse;
import co.ke.integration.mpesa.dto.response.balance.BalanceResponse;
import co.ke.integration.mpesa.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * Controller for handling M-Pesa Account Balance operations.
 * Provides endpoints for:
 * 1. Querying account balance
 * 2. Handling balance query callbacks from M-Pesa i.e Callback processing & Timeout handling
 * The callback and timeout endpoints will be called by M-Pesa's systems based on the URLs you provide in the initial request.
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/mpesa/balance")
@RequiredArgsConstructor
@Tag(name = "M-Pesa Balance", description = "M-Pesa Balance Query APIs")
public class BalanceController {

    private final BalanceService balanceService;

    /**
     * Initiates an account balance query to M-Pesa.
     *
     * @param request Balance query request
     * @return ResponseEntity containing the query acknowledgment
     */
    @PostMapping("/query")
    @Operation(
            summary = "Query M-Pesa account balance",
            description = "Initiates a balance query for the specified shortcode"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance query initiated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BalanceResponse> queryBalance(
            @Valid @RequestBody BalanceRequest request
    ) {
        log.info("Processing balance query request for shortcode: {}", request.getPartyA());
        BalanceResponse response = balanceService.queryBalance(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles the callback response from M-Pesa for balance query.
     *
     * @param callbackResponse Balance query result from M-Pesa
     * @return ResponseEntity acknowledging receipt of callback
     */
    @PostMapping("/callback")
    @Operation(
            summary = "Handle M-Pesa balance query callback",
            description = "Processes the balance query result received from M-Pesa"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Callback processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid callback data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> handleBalanceCallback(
            @Valid @RequestBody BalanceCallbackResponse callbackResponse
    ) {
        log.info("Received balance query callback for conversation ID: {}",
                callbackResponse.getResult().getConversationID());

        balanceService.processBalanceCallback(callbackResponse);
        return ResponseEntity.ok("Balance callback processed successfully");
    }

    /**
     * Handles timeout notifications from M-Pesa for balance queries.
     *
     * @param timeoutResponse Timeout notification from M-Pesa
     * @return ResponseEntity acknowledging receipt of timeout notification
     */
    @PostMapping("/timeout")
    @Operation(
            summary = "Handle M-Pesa balance query timeout",
            description = "Processes timeout notifications for balance queries"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Timeout notification processed"),
            @ApiResponse(responseCode = "400", description = "Invalid timeout data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> handleBalanceTimeout(
            @Valid @RequestBody BalanceCallbackResponse timeoutResponse
    ) {
        log.warn("Received balance query timeout for conversation ID: {}",
                timeoutResponse.getResult().getConversationID());

        balanceService.processBalanceTimeout(timeoutResponse);
        return ResponseEntity.ok("Balance timeout processed successfully");
    }
}