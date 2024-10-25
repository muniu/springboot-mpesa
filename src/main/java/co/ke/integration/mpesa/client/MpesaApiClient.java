package co.ke.integration.mpesa.client;

import co.ke.integration.mpesa.config.MpesaConfig;
import co.ke.integration.mpesa.config.MpesaMetrics;
import co.ke.integration.mpesa.dto.response.AuthResponse;
import co.ke.integration.mpesa.exception.MpesaApiException;
import co.ke.integration.mpesa.exception.MpesaErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * Centralized client for all M-Pesa API communications.
 * Handles authentication, request/response processing, and error handling.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MpesaApiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final MpesaConfig mpesaConfig;
    private final MpesaMetrics metrics;

    /**
     * Makes an authenticated request to M-Pesa API.
     *
     * @param url          The API endpoint URL
     * @param request      The request object
     * @param responseType The expected response type
     * @param accessToken  Valid access token
     * @param <T>          Request type
     * @param <R>          Response type
     * @return Response from M-Pesa API
     * @throws MpesaApiException if the request fails
     */
    public <T, R> R call(String url, T request, Class<R> responseType, String accessToken) {
        String requestId = UUID.randomUUID().toString();
        try {
            HttpEntity<T> requestEntity = createRequestEntity(request, accessToken);
            logRequest(url, request, requestId);

            ResponseEntity<R> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    responseType
            );

            logResponse(response, requestId);
            return response.getBody();

        } catch (Exception e) {
            handleApiError(e, requestId);
            throw new MpesaApiException(MpesaErrorCode.INTERNAL_SERVER_ERROR, requestId);
        }
    }

    /**
     * Generates authentication token from M-Pesa API.
     *
     * @return AuthResponse containing the access token
     * @throws MpesaApiException if authentication fails
     */
    public AuthResponse authenticate() {
        String requestId = UUID.randomUUID().toString();
        try {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<AuthResponse> response = restTemplate.exchange(
                    mpesaConfig.getAuthurl() + "?grant_type=client_credentials",
                    HttpMethod.GET,
                    request,
                    AuthResponse.class
            );

            if (response.getBody() == null) {
                throw new MpesaApiException(MpesaErrorCode.BAD_REQUEST, requestId);
            }

            return response.getBody();

        } catch (Exception e) {
            handleApiError(e, requestId);
            throw new MpesaApiException(MpesaErrorCode.INVALID_ACCESS_TOKEN, requestId);
        }
    }

    private <T> HttpEntity<T> createRequestEntity(T request, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        return new HttpEntity<>(request, headers);
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(
                mpesaConfig.getConsumerKey(),
                mpesaConfig.getConsumerSecret()
        );
        return headers;
    }

    private <T> void logRequest(String url, T request, String requestId) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("M-Pesa API Request [{}]: URL: {}, Payload: {}",
                        requestId, url, objectMapper.writeValueAsString(request));
            }
        } catch (Exception e) {
            log.warn("Could not log request details [{}]", requestId, e);
        }
    }

    private void logResponse(ResponseEntity<?> response, String requestId) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("M-Pesa API Response [{}]: Status: {}, Body: {}",
                        requestId,
                        response.getStatusCode(),
                        objectMapper.writeValueAsString(response.getBody())
                );
            }
        } catch (Exception e) {
            log.warn("Could not log response details [{}]", requestId, e);
        }
    }

    private void handleApiError(Exception e, String requestId) {
        if (e instanceof HttpClientErrorException clientError) {
            handleHttpClientError(clientError, requestId);
        } else if (e instanceof HttpServerErrorException serverError) {
            handleHttpServerError(serverError, requestId);
        } else if (e instanceof ResourceAccessException resourceError) {
            handleResourceAccessError(resourceError, requestId);
        } else {
            log.error("Unexpected error during API call [{}]", requestId, e);
            throw new MpesaApiException(MpesaErrorCode.INTERNAL_SERVER_ERROR, requestId, e);
        }
    }

    private void handleHttpClientError(HttpClientErrorException e, String requestId) {
        String responseBody = e.getResponseBodyAsString();
        log.error("Client error during API call [{}]: {}", requestId, responseBody);

        try {
            ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
            MpesaErrorCode errorCode = MpesaErrorCode.fromCode(errorResponse.getErrorCode());
            throw new MpesaApiException(errorCode, requestId, e);
        } catch (Exception ex) {
            throw new MpesaApiException(MpesaErrorCode.BAD_REQUEST, requestId, e);
        }
    }

    private void handleHttpServerError(HttpServerErrorException e, String requestId) {
        log.error("Server error during API call [{}]: {}", requestId, e.getResponseBodyAsString());
        throw new MpesaApiException(MpesaErrorCode.INTERNAL_SERVER_ERROR, requestId, e);
    }

    private void handleResourceAccessError(ResourceAccessException e, String requestId) {
        log.error("Network error during API call [{}]", requestId, e);
        throw new MpesaApiException(MpesaErrorCode.INTERNAL_SERVER_ERROR, requestId, e);
    }

    @lombok.Data
    private static class ErrorResponse {
        private String requestId;
        private String errorCode;
        private String errorMessage;
    }
}
