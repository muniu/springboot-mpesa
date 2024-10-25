package co.ke.integration.mpesa.service;

import co.ke.integration.mpesa.config.MpesaConfig;
import co.ke.integration.mpesa.config.MpesaMetrics;
import co.ke.integration.mpesa.dto.auth.AuthToken;
import co.ke.integration.mpesa.dto.response.AuthResponse;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@Slf4j
public class AuthService {
    private final AuthToken authToken;
    private final MpesaConfig mpesaConfig;
    private final RestTemplate restTemplate;

    private final MpesaMetrics metrics;

    @Autowired
    public AuthService(AuthToken authToken,
                       MpesaConfig mpesaConfig,
                       RestTemplate restTemplate,
                       MpesaMetrics metrics) {
        this.authToken = authToken;
        this.mpesaConfig = mpesaConfig;
        this.restTemplate = restTemplate;
        this.metrics=metrics;
    }
    public synchronized String getAccessToken() {
        if (authToken.isValid()) {
            return authToken.getToken();
        }
        generateNewAccessToken();
        return authToken.getToken();
    }

    private void generateNewAccessToken() {
        Timer.Sample sample = metrics.startAuthTimer();//collecting metrics
        try {
            metrics.incrementAuthRequests();
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(mpesaConfig.getConsumerkey(), mpesaConfig.getConsumerSecret());

            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<AuthResponse> response = restTemplate.exchange(
                    mpesaConfig.getAuthurl() + "?grant_type=client_credentials",
                    HttpMethod.GET,
                    request,
                    AuthResponse.class
            );

            if (response.getBody() == null) {
                //throw new AuthenticationException("Failed to get access token from M-Pesa");
            }

            // Update the currentToken with the new access token and expiry time
            authToken.update(
                    response.getBody().getAccessToken(),
                    response.getBody().getExpiresInSeconds()
            );

        } catch (RestClientException e) {
            metrics.incrementAuthFailures();//update the failure tag
            //throw new AuthenticationException("Failed to authenticate with M-Pesa", e);
        }finally {
            metrics.stopAuthTimer(sample);//stop the metrics timer
        }
    }
}