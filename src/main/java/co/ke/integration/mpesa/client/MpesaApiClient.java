package co.ke.integration.mpesa.client;

import co.ke.integration.mpesa.config.MpesaConfig;
import co.ke.integration.mpesa.config.MpesaMetrics;
import co.ke.integration.mpesa.dto.response.AuthResponse;
import co.ke.integration.mpesa.exception.MpesaApiException;
import co.ke.integration.mpesa.exception.MpesaErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Timer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.net.http.HttpHeaders;
import java.util.UUID;

/**
 * Centralized client for making M-Pesa API calls.
 * Handles all HTTP communication with M-Pesa APIs including authentication,
 * request preparation, and response handling.
 *
 * @author Muniu Kariuki
 * @version 1.0
 * @since 2024-01-25
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MpesaApiClient {

    private final RestTemplate restTemplate;
    private final MpesaConfig mpesaConfig;
    private final ObjectMapper objectMapper;
    private final MpesaMetrics metrics;

    /**
     * Generate OAuth token for M-Pesa API access.
     *
     * @return AuthResponse containing the access token
     * @throws MpesaApiException if authentication fails
     */
    public AuthResponse authenticate() {
        Timer.Sample timer = metrics.startAuthTimer();
        String requestId = generateRequestId();

        try {
            log.debug("Initiating M-Pesa authentication request [{}]", requestId);
            //metrics.incrementAuthRequests();

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(mpesaConfig.getConsumerKey(), mpesaConfig.getConsumerSecret());

            HttpEntity<String> request = new HttpEntity<>(headers);
            String url = mpesaConfig.getAuthUrl() + "?grant_type=client_credentials";

            ResponseEntity<AuthResponse> response = executeRequest(
                    url,
                    HttpMethod.GET,
                    request,
                    AuthResponse.class,
                    requestId
            );

            log.info("Successfully obtained auth token [{}]", requestId);
            return response.getBody();

        } catch (Exception e) {
            metrics.incrementAuthFailures();
            handleAuthenticationError(e, requestId);
            return null; // Never reached, handleAuthenticationError will throw
        } finally {
            //timer.stop(metrics.getAuthTimer());
        }
    }

    /**
     * Make a POST request to M-Pesa API.
     *
     * @param url API endpoint
     * @param request Request body
     * @param responseType Expected response type
     * @param operationType Operation being performed (for metrics)
     * @param accessToken Valid access token
     * @return Response from the API
     * @throws MpesaApiException if the API call fails
     */
    public <T, R> R post(String url, T request, Class<R> responseType,
                         String operationType, String accessToken) {
        //Timer.Sample timer = metrics.startOperationTimer(operationType);
        String requestId = generateRequestId();

        try {
            log.debug("Making M-Pesa API request - Type: {} [{}]", operationType, requestId);
            //metrics.incrementRequests(operationType);

            HttpHeaders headers = createHeaders(accessToken);
            logRequest(request, url, requestId);

            HttpEntity<T> requestEntity = new HttpEntity<>(request, headers);
            ResponseEntity<R> response = executeRequest(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    responseType,
                    requestId
            );

            logResponse(response, requestId);
            return response.getBody();

        } catch (Exception e) {
            //metrics.incrementFailures(operationType);
            handleApiError(e, requestId, operationType);
            return null; // Never reached, handleApiError will throw
        } finally {
            //timer.stop(metrics.getOperationTimer(operationType));
        }
    }

    /**
     * Execute HTTP request with retry logic and error handling.
     */
    private <T, R> ResponseEntity<R> executeRequest(String url, HttpMethod method,
                                                    HttpEntity<?> requestEntity, Class<R> responseType, String requestId) {
        try {
            return restTemplate.exchange(
                    url,
                    method,
                    requestEntity,
                    responseType
            );
        } catch (HttpClientErrorException e) {
            handleHttpClientError(e, requestId);
            return null; // Never reached, handleHttpClientError will throw
        } catch (HttpServerErrorException e) {
            handleHttpServerError(e, requestId);
            return null; // Never reached, handleHttpServerError will throw
        } catch (ResourceAccessException e) {
            handleResourceAccessError(e, requestId);
            return null; // Never reached, handleResourceAccessError will throw
        }
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        return headers;
    }

    /**
     * Generic error handler for all M-Pesa API errors.
     * Maps different types of errors to appropriate exceptions.
     *
     * @param e The caught exception
     * @param requestId The unique request identifier
     * @param operationType The type of operation that failed
     * @throws MpesaApiException with appropriate error details
     */
    private void handleApiError(Exception e, String requestId, String operationType) {
        log.error("Error during {} operation [{}]: {}", operationType, requestId, e.getMessage());

        if (e instanceof HttpClientErrorException clientError) {
            handleHttpClientError(clientError, requestId);
        }
        else if (e instanceof HttpServerErrorException serverError) {
            handleHttpServerError(serverError, requestId);
        }
        else if (e instanceof ResourceAccessException resourceError) {
            handleResourceAccessError(resourceError, requestId);
        }
        else if (e instanceof HttpMessageNotReadableException messageError) {
            log.error("Invalid response format [{}]: {}", requestId, messageError.getMessage());
            throw new MpesaApiException(MpesaErrorCode.INVALID_RESPONSE_FORMAT, requestId, e);
        }
        else {
            log.error("Unexpected error during API call [{}]: {}", requestId, e.getMessage());
            throw new MpesaApiException(MpesaErrorCode.INTERNAL_SERVER_ERROR, requestId, e);
        }
    }


    private void handleHttpClientError(HttpClientErrorException e, String requestId) {
        String responseBody = e.getResponseBodyAsString();
        try {
            ErrorDto errorDto = objectMapper.readValue(responseBody, ErrorDto.class);
            MpesaErrorCode errorCode = MpesaErrorCode.fromCode(errorDto.getErrorCode());

            if (errorCode != null) {
                log.error("M-Pesa API client error: {} [{}]", errorCode.getMessage(), requestId);
                throw new MpesaApiException(errorCode, requestId);
            } else {
                log.error("Unknown M-Pesa API error: {} [{}]", responseBody, requestId);
                throw new MpesaApiException(MpesaErrorCode.INTERNAL_SERVER_ERROR, requestId);
            }
        } catch (JsonProcessingException ex) {
            log.error("Error parsing M-Pesa error response: {} [{}]", responseBody, requestId);
            throw new MpesaApiException(MpesaErrorCode.INTERNAL_SERVER_ERROR, requestId);
        }
    }

    private void handleHttpServerError(HttpServerErrorException e, String requestId) {
        log.error("M-Pesa API server error: {} [{}]", e.getMessage(), requestId);
        throw new MpesaApiException(MpesaErrorCode.INTERNAL_SERVER_ERROR, requestId, e);
    }

    private void handleResourceAccessError(ResourceAccessException e, String requestId) {
        log.error("M-Pesa API connection error: {} [{}]", e.getMessage(), requestId);
        throw new MpesaApiException(
                e.getCause() instanceof SocketTimeoutException ?
                        MpesaErrorCode.REQUEST_TIMEOUT :
                        MpesaErrorCode.CONNECTION_ERROR,
                requestId,
                e
        );
    }

    private void handleAuthenticationError(Exception e, String requestId) {
        log.error("M-Pesa authentication error: {} [{}]", e.getMessage(), requestId);
        if (e instanceof HttpClientErrorException.Unauthorized) {
            throw new MpesaApiException(MpesaErrorCode.INVALID_ACCESS_TOKEN, requestId, e);
        }
        throw new MpesaApiException(MpesaErrorCode.AUTH_ERROR, requestId, e);
    }

    private void logRequest(Object request, String url, String requestId) {
        if (log.isDebugEnabled()) {
            try {
                log.debug("M-Pesa API request [{}]: URL: {}, Body: {}",
                        requestId, url, objectMapper.writeValueAsString(request));
            } catch (JsonProcessingException e) {
                log.warn("Could not log request body [{}]", requestId);
            }
        }
    }

    private void logResponse(ResponseEntity<?> response, String requestId) {
        if (log.isDebugEnabled()) {
            try {
                log.debug("M-Pesa API response [{}]: Status: {}, Body: {}",
                        requestId, response.getStatusCode(),
                        objectMapper.writeValueAsString(response.getBody()));
            } catch (JsonProcessingException e) {
                log.warn("Could not log response body [{}]", requestId);
            }
        }
    }

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    /**
     * DTO for error responses from M-Pesa API.
     */
    @Data
    private static class ErrorDto {
        private String requestId;
        private String errorCode;
        private String errorMessage;
    }
}