package co.ke.integration.mpesa.service;

import co.ke.integration.mpesa.client.MpesaApiClient;
import co.ke.integration.mpesa.config.MpesaConfig;
import co.ke.integration.mpesa.dto.request.c2b.C2BPaymentRequest;
import co.ke.integration.mpesa.dto.request.c2b.C2BRegisterUrlRequest;
import co.ke.integration.mpesa.dto.response.c2b.C2BConfirmationResponse;
import co.ke.integration.mpesa.dto.response.c2b.C2BRegisterUrlResponse;
import co.ke.integration.mpesa.dto.response.c2b.C2BValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.CompletableFuture;

/**
 * Service for handling M-Pesa C2B operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class C2BService {

    private final MpesaApiClient mpesaApiClient;
    private final AuthService authService;
    private final MpesaConfig mpesaConfig;
    private final C2BService c2bService;

    /**
     * Register URLs for C2B callbacks.
     * This is typically a one-time operation unless URLs need to be updated.
     *
     * @param request The URL registration request
     * @return Response from M-Pesa
     */
    public C2BRegisterUrlResponse registerUrls(C2BRegisterUrlRequest request) {
        log.info("Registering C2B URLs for shortcode: {}", request.getShortCode());

        String accessToken = authService.getAccessToken();

        return mpesaApiClient.call(
                mpesaConfig.getC2bregisterUrl(),
                request,
                C2BRegisterUrlResponse.class,
                accessToken
        );
    }

    /**
     * Process validation request from M-Pesa asynchronously.
     */
    @Async
    public CompletableFuture<C2BValidationResponse> handleValidation(C2BPaymentRequest request) {
        log.info("Processing C2B validation request for transaction: {}",
                request.getTransactionId());

        try {
            // Quick validation that needs to be done synchronously
            if (!isBasicValidationPassed(request)) {
                return CompletableFuture.completedFuture(
                        C2BValidationResponse.rejected("C2B00012", "Invalid Account Number")
                );
            }

            // Return accepted response immediately
            processValidationAsync(request);
            return CompletableFuture.completedFuture(C2BValidationResponse.accepted());

        } catch (Exception e) {
            log.error("Error in validation request for transaction: {}",
                    request.getTransactionId(), e);
            return CompletableFuture.completedFuture(
                    C2BValidationResponse.rejected("C2B00016", "Internal Error")
            );
        }
    }

    /**
     * Process confirmation request from M-Pesa asynchronously.
     */
    @Async
    public CompletableFuture<C2BConfirmationResponse> handleConfirmation(C2BPaymentRequest request) {
        log.info("Processing C2B confirmation for transaction: {}",
                request.getTransactionId());

        // Return success response immediately
        processConfirmationAsync(request);
        return CompletableFuture.completedFuture(C2BConfirmationResponse.success());
    }

    /**
     * Process validation asynchronously after returning response to M-Pesa.
     */
    @Async
    protected void processValidationAsync(C2BPaymentRequest request) {
        try {
            // Detailed validation and business logic
            // Database operations
            // Notifications
            // Any other time-consuming operations
            log.info("Async validation processing completed for transaction: {}",
                    request.getTransactionId());
        } catch (Exception e) {
            log.error("Error in async validation processing for transaction: {}",
                    request.getTransactionId(), e);
        }
    }

    /**
     * Process confirmation asynchronously after returning response to M-Pesa.
     */
    @Async
    protected void processConfirmationAsync(C2BPaymentRequest request) {
        try {
            // Payment processing
            // Database updates
            // Notifications
            // Receipt generation
            // Any other time-consuming operations
            log.info("Async confirmation processing completed for transaction: {}",
                    request.getTransactionId());
        } catch (Exception e) {
            log.error("Error in async confirmation processing for transaction: {}",
                    request.getTransactionId(), e);
        }
    }

    private boolean isBasicValidationPassed(C2BPaymentRequest request) {
        // Quick validations that need to be done before responding
        return request.getBillRefNumber() != null &&
                !request.getBillRefNumber().isEmpty();
    }

    /**
     *
     * Handle validation requests from M-Pesa.
     * @return
     */
    @PostMapping("/validation")
    public CompletableFuture<ResponseEntity<C2BValidationResponse>> validation(
            @RequestBody C2BPaymentRequest request) {
        log.info("Received C2B validation request for transaction: {}",
                request.getTransactionId());

        return c2bService.handleValidation(request)
                .thenApply(ResponseEntity::ok);
    }

    /**
     * Handle confirmation requests from M-Pesa.
     */
    @PostMapping("/confirmation")
    public CompletableFuture<ResponseEntity<C2BConfirmationResponse>> confirmation(
            @RequestBody C2BPaymentRequest request) {
        log.info("Received C2B confirmation for transaction: {}",
                request.getTransactionId());

        return c2bService.handleConfirmation(request)
                .thenApply(ResponseEntity::ok);
    }
}