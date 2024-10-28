package co.ke.integration.mpesa.controller;

import co.ke.integration.mpesa.dto.request.b2c.B2CRequest;
import co.ke.integration.mpesa.dto.response.b2c.B2CCallbackResponse;
import co.ke.integration.mpesa.dto.response.b2c.B2CResponse;
import co.ke.integration.mpesa.service.B2CService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/mpesa/b2c")
@RequiredArgsConstructor
public class B2CController {

    private final B2CService b2cService;

    @PostMapping("/payment")
    public ResponseEntity<B2CResponse> processPayment(@RequestBody B2CRequest request) {
        log.info("Received B2C payment request for: {}", request.getPartyB());
        B2CResponse response = b2cService.processB2CPayment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody B2CCallbackResponse callback) {
        log.info("Received B2C callback for transaction: {}",
                callback.getResult().getTransactionID());
        b2cService.handleB2CCallback(callback);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/timeout")
    public ResponseEntity<String> handleTimeout(@RequestBody B2CCallbackResponse timeoutResponse) {
        log.info("Received B2C timeout for transaction: {}",
                timeoutResponse.getResult().getTransactionID());
        b2cService.handleB2CTimeout(timeoutResponse);
        return ResponseEntity.ok("Success");
    }
}
