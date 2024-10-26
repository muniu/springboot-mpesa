package co.ke.integration.mpesa.controller;

import co.ke.integration.mpesa.dto.request.c2b.C2BRegisterUrlRequest;
import co.ke.integration.mpesa.dto.response.c2b.C2BRegisterUrlResponse;
import co.ke.integration.mpesa.service.C2BService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for M-Pesa C2B operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/mpesa/c2b")
@RequiredArgsConstructor
public class C2BController {

    private final C2BService c2bService;

    /**
     * Register URLs for C2B callbacks.
     *
     * @param request The URL registration request
     * @return Response from M-Pesa
     */
    @PostMapping("/register-urls")
    public ResponseEntity<C2BRegisterUrlResponse> registerUrls(
            @RequestBody C2BRegisterUrlRequest request) {
        log.info("Received C2B URL registration request for shortcode: {}",
                request.getShortCode());

        C2BRegisterUrlResponse response = c2bService.registerUrls(request);
        return ResponseEntity.ok(response);
    }
}
