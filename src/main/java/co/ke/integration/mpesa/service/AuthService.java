package co.ke.integration.mpesa.service;

import co.ke.integration.mpesa.config.MpesaConfig;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class AuthService {
    private final MpesaConfig mpesaConfig;

    // Token holder
    private AuthToken currentToken;

    public AuthService(MpesaConfig mpesaConfig) {
        this.mpesaConfig = mpesaConfig;
    }

    // Method to fetch the access token
    // synchronized to make sure multiple concurrent requests do not trigger multiple token refreshes.
    public synchronized String getAccessToken() throws Exception {
        // Check if the token exists and is not expired
        if (currentToken == null || currentToken.isExpired()) {
            // Fetch a new token
            fetchNewToken();
        }
        return currentToken.getToken();
    }

    // Method to make an API request to fetch a new token
    private void fetchNewToken() throws Exception {
        String credentials = Base64.getEncoder().encodeToString((mpesaConfig.getconsumerKey() + ":" + mpesaConfig.getConsumerSecret()).getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + credentials);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the request to the auth endpoint
        ResponseEntity<String> response = restTemplate.exchange(mpesaConfig.getAuthurl(), HttpMethod.GET, entity, String.class);

        // Parse the JSON response
        JSONObject jsonObj = new JSONObject(response.getBody());
        String accessToken = jsonObj.getString("access_token");
        long expiresIn = jsonObj.getLong("expires_in");

        // Update the currentToken with the new access token and expiry time
        currentToken = new AuthToken(accessToken, expiresIn);
    }
}
