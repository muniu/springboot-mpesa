package co.ke.integration.mpesa.service;

import co.ke.integration.mpesa.client.MpesaApiClient;
import co.ke.integration.mpesa.config.MpesaConfig;
import co.ke.integration.mpesa.dto.request.b2c.B2CRequest;
import co.ke.integration.mpesa.dto.response.b2c.B2CCallbackResponse;
import co.ke.integration.mpesa.dto.response.b2c.B2CResponse;
import co.ke.integration.mpesa.exception.MpesaApiException;
import co.ke.integration.mpesa.exception.MpesaErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class B2CService {

    private final MpesaApiClient mpesaApiClient;
    private final AuthService authService;
    private final MpesaConfig mpesaConfig;

    public B2CResponse processB2CPayment(B2CRequest request) {
        String requestId = UUID.randomUUID().toString();
        validateRequest(request, requestId);

        log.info("Processing B2C payment request [{}]. CommandID: {}, Amount: {}, PartyB: {}",
                requestId, request.getCommandID(), request.getAmount(), request.getPartyB());

        try {
            String accessToken = authService.getAccessToken();

            return mpesaApiClient.call(
                    mpesaConfig.getB2curl(),
                    request,
                    B2CResponse.class,
                    accessToken
            );
        } catch (Exception e) {
            log.error("Error processing B2C payment [{}]", requestId, e);
            throw new MpesaApiException(MpesaErrorCode.INTERNAL_SERVER_ERROR, requestId, e);
        }
    }

    public void handleB2CCallback(B2CCallbackResponse callback) {
        String requestId = UUID.randomUUID().toString();
        String transactionId = callback.getResult().getTransactionID();
        log.info("Processing B2C callback [{}] for transaction: {}", requestId, transactionId);

        try {
            if ("0".equals(callback.getResult().getResultCode())) {
                log.info("B2C payment successful [{}] for transaction: {}", requestId, transactionId);
                processSuccessfulB2CPayment(callback);
            } else {
                log.error("B2C payment failed [{}]. TransactionID: {}, ResultCode: {}, ResultDesc: {}",
                        requestId,
                        transactionId,
                        callback.getResult().getResultCode(),
                        callback.getResult().getResultDesc()
                );
                processFailedB2CPayment(callback);
            }
        } catch (Exception e) {
            log.error("Error processing B2C callback [{}]", requestId, e);
            throw new MpesaApiException(MpesaErrorCode.INTERNAL_SERVER_ERROR, requestId, e);
        }
    }

    private void validateRequest(B2CRequest request, String requestId) {
        if (request == null) {
            throw new MpesaApiException(MpesaErrorCode.INVALID_REQUEST_PAYLOAD, requestId);
        }

        if (!isValidCommandId(request.getCommandID())) {
            throw new MpesaApiException(MpesaErrorCode.INVALID_COMMAND, requestId);
        }

        if (!isValidPhoneNumber(request.getPartyB())) {
            throw new MpesaApiException(MpesaErrorCode.INVALID_REQUEST_PARAMETERS, requestId);
        }

        if (!isValidAmount(request.getAmount())) {
            throw new MpesaApiException(MpesaErrorCode.INVALID_INPUT, requestId);
        }
    }

    private boolean isValidCommandId(String commandId) {
        if (commandId == null) return false;
        return switch (commandId) {
            case "SalaryPayment", "BusinessPayment", "PromotionPayment" -> true;
            default -> false;
        };
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return false;
        return phoneNumber.matches("^254[7|1]\\d{8}$");
    }

    private boolean isValidAmount(String amount) {
        if (amount == null) return false;
        try {
            double value = Double.parseDouble(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void processSuccessfulB2CPayment(B2CCallbackResponse callback) {
        // TODO Implement success handling
        // e.g., update transaction status, send notifications, etc.
    }

    private void processFailedB2CPayment(B2CCallbackResponse callback) {
        // TODO Implement failure handling
        // e.g., update transaction status, retry logic if needed, etc.
    }

    public void handleB2CTimeout(B2CCallbackResponse timeoutResponse) {
        // TODO handle scenario where the API returns a timeout.
    }
}